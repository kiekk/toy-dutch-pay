import { useState, useCallback, useEffect } from 'react';
import { storageService } from '../services/storage/storageService';
import type { Gathering, Participant, SettlementRound, Exclusion, GatheringType } from '../types';

interface UseGatheringsReturn {
  gatherings: Gathering[];
  isLoading: boolean;
  error: string | null;
  fetchGatherings: () => void;
  createGathering: (name: string, type: GatheringType, startDate: string, endDate: string) => Gathering | null;
  updateGathering: (id: string, name: string, type: GatheringType, startDate: string, endDate: string) => Gathering | null;
  deleteGathering: (id: string) => boolean;
  addParticipant: (gatheringId: string, name: string) => Participant | null;
  removeParticipant: (gatheringId: string, participantId: string) => boolean;
  addRound: (gatheringId: string, title: string, amount: number, payerId: string, exclusions?: Exclusion[]) => SettlementRound | null;
  updateRound: (gatheringId: string, roundId: string, title: string, amount: number, payerId: string, exclusions?: Exclusion[]) => SettlementRound | null;
  deleteRound: (gatheringId: string, roundId: string) => boolean;
  updateLocalGathering: (id: string, updates: Partial<Gathering>) => void;
}

export const useGatherings = (): UseGatheringsReturn => {
  const [gatherings, setGatherings] = useState<Gathering[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchGatherings = useCallback(() => {
    setGatherings(storageService.getGatherings());
    setIsLoading(false);
  }, []);

  useEffect(() => {
    fetchGatherings();
  }, [fetchGatherings]);

  const createGathering = useCallback((
    name: string,
    type: GatheringType,
    startDate: string,
    endDate: string
  ): Gathering | null => {
    try {
      const gathering: Gathering = {
        id: crypto.randomUUID(),
        name,
        type,
        startDate: new Date(startDate).getTime(),
        endDate: new Date(endDate).getTime(),
        createdAt: Date.now(),
        participants: [],
        rounds: [],
      };
      storageService.saveGathering(gathering);
      setGatherings(prev => [gathering, ...prev]);
      return gathering;
    } catch {
      setError('모임 생성에 실패했습니다.');
      return null;
    }
  }, []);

  const updateGathering = useCallback((
    id: string,
    name: string,
    type: GatheringType,
    startDate: string,
    endDate: string
  ): Gathering | null => {
    const updates = {
      name,
      type,
      startDate: new Date(startDate).getTime(),
      endDate: new Date(endDate).getTime(),
    };
    const updated = storageService.updateGathering(id, updates);
    if (updated) {
      setGatherings(prev => prev.map(g => g.id === id ? { ...g, ...updates } : g));
    }
    return updated;
  }, []);

  const deleteGathering = useCallback((id: string): boolean => {
    const ok = storageService.deleteGathering(id);
    if (ok) setGatherings(prev => prev.filter(g => g.id !== id));
    return ok;
  }, []);

  const addParticipant = useCallback((gatheringId: string, name: string): Participant | null => {
    const newParticipant: Participant = { id: crypto.randomUUID(), name };
    setGatherings(prev => {
      const updated = prev.map(g => {
        if (g.id !== gatheringId) return g;
        return { ...g, participants: [...g.participants, newParticipant] };
      });
      const target = updated.find(g => g.id === gatheringId);
      if (target) storageService.updateGathering(gatheringId, { participants: target.participants });
      return updated;
    });
    return newParticipant;
  }, []);

  const removeParticipant = useCallback((gatheringId: string, participantId: string): boolean => {
    setGatherings(prev => {
      const updated = prev.map(g => {
        if (g.id !== gatheringId) return g;
        const updatedParticipants = g.participants.filter(p => p.id !== participantId);
        const updatedRounds = g.rounds.map(r => ({
          ...r,
          payerId: r.payerId === participantId ? (updatedParticipants[0]?.id || '') : r.payerId,
          excluded: r.excluded.filter(e => e.participantId !== participantId),
        }));
        return { ...g, participants: updatedParticipants, rounds: updatedRounds };
      });
      const target = updated.find(g => g.id === gatheringId);
      if (target) storageService.updateGathering(gatheringId, { participants: target.participants, rounds: target.rounds });
      return updated;
    });
    return true;
  }, []);

  const addRound = useCallback((
    gatheringId: string,
    title: string,
    amount: number,
    payerId: string,
    exclusions?: Exclusion[]
  ): SettlementRound | null => {
    const newRound: SettlementRound = {
      id: crypto.randomUUID(),
      title,
      amount,
      payerId,
      excluded: exclusions || [],
    };
    setGatherings(prev => {
      const updated = prev.map(g => {
        if (g.id !== gatheringId) return g;
        return { ...g, rounds: [...g.rounds, newRound] };
      });
      const target = updated.find(g => g.id === gatheringId);
      if (target) storageService.updateGathering(gatheringId, { rounds: target.rounds });
      return updated;
    });
    return newRound;
  }, []);

  const updateRound = useCallback((
    gatheringId: string,
    roundId: string,
    title: string,
    amount: number,
    payerId: string,
    exclusions?: Exclusion[]
  ): SettlementRound | null => {
    const updatedRound: SettlementRound = {
      id: roundId,
      title,
      amount,
      payerId,
      excluded: exclusions || [],
    };
    setGatherings(prev => {
      const updated = prev.map(g => {
        if (g.id !== gatheringId) return g;
        return { ...g, rounds: g.rounds.map(r => r.id === roundId ? updatedRound : r) };
      });
      const target = updated.find(g => g.id === gatheringId);
      if (target) storageService.updateGathering(gatheringId, { rounds: target.rounds });
      return updated;
    });
    return updatedRound;
  }, []);

  const deleteRound = useCallback((gatheringId: string, roundId: string): boolean => {
    setGatherings(prev => {
      const updated = prev.map(g => {
        if (g.id !== gatheringId) return g;
        return { ...g, rounds: g.rounds.filter(r => r.id !== roundId) };
      });
      const target = updated.find(g => g.id === gatheringId);
      if (target) storageService.updateGathering(gatheringId, { rounds: target.rounds });
      return updated;
    });
    return true;
  }, []);

  const updateLocalGathering = useCallback((id: string, updates: Partial<Gathering>) => {
    setGatherings(prev => prev.map(g => g.id === id ? { ...g, ...updates } : g));
    storageService.updateGathering(id, updates);
  }, []);

  return {
    gatherings,
    isLoading,
    error,
    fetchGatherings,
    createGathering,
    updateGathering,
    deleteGathering,
    addParticipant,
    removeParticipant,
    addRound,
    updateRound,
    deleteRound,
    updateLocalGathering,
  };
};

export default useGatherings;
