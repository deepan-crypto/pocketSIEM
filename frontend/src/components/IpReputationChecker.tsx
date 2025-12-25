import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
} from 'react-native';
import { useThreatIntelligence } from '../hooks/useThreatIntelligence';

export const IpReputationChecker: React.FC = () => {
  const [ipAddress, setIpAddress] = useState('');
  const { isLoading, error, reputation, checkIpReputation, clearError } = useThreatIntelligence();

  const handleCheck = () => {
    if (ipAddress.trim()) {
      clearError();
      checkIpReputation(ipAddress.trim());
    }
  };

  const getRiskColor = (score: number): string => {
    if (score < 30) return '#4CAF50';
    if (score < 70) return '#FF9800';
    return '#F44336';
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>🛡️ IP Reputation Checker</Text>

      <TextInput
        style={styles.input}
        placeholder="Enter IP address (e.g., 8.8.8.8)"
        value={ipAddress}
        onChangeText={setIpAddress}
        keyboardType="numeric"
        autoCapitalize="none"
      />

      <TouchableOpacity
        style={[styles.button, isLoading && styles.buttonDisabled]}
        onPress={handleCheck}
        disabled={isLoading || !ipAddress.trim()}
      >
        {isLoading ? (
          <ActivityIndicator color="#fff" />
        ) : (
          <Text style={styles.buttonText}>Check Reputation</Text>
        )}
      </TouchableOpacity>

      {error && (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>⚠️ {error}</Text>
        </View>
      )}

      {reputation && (
        <View style={styles.resultContainer}>
          <Text style={styles.resultTitle}>Results for {reputation.ip}</Text>

          <View style={styles.resultRow}>
            <Text style={styles.label}>Risk Score:</Text>
            <Text style={[styles.value, { color: getRiskColor(reputation.riskScore) }]}>
              {reputation.riskScore}/100
            </Text>
          </View>

          <View style={styles.resultRow}>
            <Text style={styles.label}>Category:</Text>
            <Text style={styles.value}>{reputation.category}</Text>
          </View>

          <View style={styles.resultRow}>
            <Text style={styles.label}>Status:</Text>
            <Text style={styles.value}>{reputation.status}</Text>
          </View>
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    color: '#333',
    textAlign: 'center',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    marginBottom: 16,
    backgroundColor: '#fff',
  },
  button: {
    backgroundColor: '#2196F3',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
  },
  buttonDisabled: {
    backgroundColor: '#ccc',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  errorContainer: {
    marginTop: 16,
    padding: 12,
    backgroundColor: '#ffebee',
    borderRadius: 8,
  },
  errorText: {
    color: '#c62828',
  },
  resultContainer: {
    marginTop: 20,
    padding: 16,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
  },
  resultTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
  },
  resultRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  label: {
    fontSize: 14,
    color: '#666',
  },
  value: {
    fontSize: 14,
    fontWeight: '600',
  },
});
