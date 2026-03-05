import type { Gathering } from '../../types';

const STORAGE_KEY = 'nbang_gatherings';

const readGatherings = (): Gathering[] => {
  const raw = localStorage.getItem(STORAGE_KEY);
  return raw ? JSON.parse(raw) : [];
};

const writeGatherings = (gatherings: Gathering[]): void => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(gatherings));
};

export const storageService = {
  getGatherings: (): Gathering[] => {
    return readGatherings();
  },

  saveGathering: (gathering: Gathering): Gathering => {
    const gatherings = readGatherings();
    gatherings.unshift(gathering);
    writeGatherings(gatherings);
    return gathering;
  },

  updateGathering: (id: string, updates: Partial<Gathering>): Gathering | null => {
    const gatherings = readGatherings();
    const index = gatherings.findIndex(g => g.id === id);
    if (index === -1) return null;
    gatherings[index] = { ...gatherings[index], ...updates };
    writeGatherings(gatherings);
    return gatherings[index];
  },

  deleteGathering: (id: string): boolean => {
    const gatherings = readGatherings();
    const filtered = gatherings.filter(g => g.id !== id);
    if (filtered.length === gatherings.length) return false;
    writeGatherings(filtered);
    return true;
  },
};
