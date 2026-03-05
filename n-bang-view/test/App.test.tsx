import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'

vi.mock('../contexts/ToastContext', () => ({
  useToast: () => ({
    showToast: vi.fn(),
  }),
}))

vi.mock('../hooks', () => ({
  useGatherings: () => ({
    gatherings: [],
    isLoading: false,
    error: null,
    createGathering: vi.fn(),
    deleteGathering: vi.fn(),
    addParticipant: vi.fn(),
    removeParticipant: vi.fn(),
    addRound: vi.fn(),
    updateRound: vi.fn(),
    deleteRound: vi.fn(),
    updateLocalGathering: vi.fn(),
  }),
}))

import App from '../App'

describe('App', () => {
  it('홈 화면이 표시되어야 함', () => {
    render(<App />)

    expect(screen.getByText('N-Bang')).toBeInTheDocument()
    expect(screen.getByText('새 모임 시작하기')).toBeInTheDocument()
  })
})
