import { apiClient } from './apiClient';
import { API_CONFIG } from '../config/api';

export interface IpReputationResponse {
  ip: string;
  riskScore: number;
  category: string;
  status: string;
  cachedAt: number;
}

export interface ThreatReportRequest {
  appName: string;
  targetIp: string;
  timestamp: number;
}

export interface ThreatReportResponse {
  id: number;
  appName: string;
  targetIp: string;
  message: string;
  createdAt: number;
}

export const threatService = {
  async checkHealth(): Promise<boolean> {
    const response = await apiClient.get<string>(API_CONFIG.ENDPOINTS.HEALTH);
    return response.status === 200;
  },

  async checkIpReputation(ip: string): Promise<IpReputationResponse | null> {
    const response = await apiClient.get<IpReputationResponse>(
      `${API_CONFIG.ENDPOINTS.REPUTATION}?ip=${encodeURIComponent(ip)}`
    );
    
    if (response.error) {
      console.error('IP reputation check failed:', response.error);
      return null;
    }
    
    return response.data;
  },

  async reportThreat(report: ThreatReportRequest): Promise<ThreatReportResponse | null> {
    const response = await apiClient.post<ThreatReportResponse>(
      API_CONFIG.ENDPOINTS.REPORT,
      report
    );
    
    if (response.error) {
      console.error('Threat report submission failed:', response.error);
      return null;
    }
    
    return response.data;
  },
};
