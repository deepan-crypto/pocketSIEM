import { useState, useCallback } from 'react';
import { 
  threatService, 
  IpReputationResponse, 
  ThreatReportRequest,
  ThreatReportResponse 
} from '../services/threatService';

interface UseThreatIntelligenceReturn {
  isLoading: boolean;
  error: string | null;
  reputation: IpReputationResponse | null;
  checkIpReputation: (ip: string) => Promise<void>;
  reportThreat: (report: ThreatReportRequest) => Promise<ThreatReportResponse | null>;
  clearError: () => void;
}

export const useThreatIntelligence = (): UseThreatIntelligenceReturn => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reputation, setReputation] = useState<IpReputationResponse | null>(null);

  const checkIpReputation = useCallback(async (ip: string) => {
    setIsLoading(true);
    setError(null);
    
    try {
      const result = await threatService.checkIpReputation(ip);
      if (result) {
        setReputation(result);
      } else {
        setError('Failed to fetch IP reputation');
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error occurred');
    } finally {
      setIsLoading(false);
    }
  }, []);

  const reportThreat = useCallback(async (report: ThreatReportRequest) => {
    setIsLoading(true);
    setError(null);
    
    try {
      const result = await threatService.reportThreat(report);
      if (!result) {
        setError('Failed to submit threat report');
      }
      return result;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error occurred');
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  return {
    isLoading,
    error,
    reputation,
    checkIpReputation,
    reportThreat,
    clearError,
  };
};
