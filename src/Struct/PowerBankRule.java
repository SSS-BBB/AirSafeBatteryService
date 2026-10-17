package Struct;

public class PowerBankRule {
	
	public int ruleId, maxCarry, maxWh;
	public boolean checkedBaggage, carryOnBaggage;
	
	public PowerBankRule() {
		this.maxCarry = -1;
		this.maxWh = -1;
		this.checkedBaggage = false;
		this.carryOnBaggage = false;
	}
	
}