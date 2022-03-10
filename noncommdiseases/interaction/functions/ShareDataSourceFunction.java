package v30.noncommdiseases.interaction.functions;

import v30.noncommdiseases.util.DataSharedBetweenAgents;

/**
 * @author Rabia AZIZA
 */
public interface ShareDataSourceFunction {
		
	public boolean isUpdatedEPhy();
	
	public void setStatus(String status);
	
		
	/**
	 * Data to be sent to other agents in the same activity
	 */
	public DataSharedBetweenAgents dataToShare();
}
