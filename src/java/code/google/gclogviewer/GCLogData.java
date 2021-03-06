/**
 * GCLogViewer
 * 
 * A free open source tool to visualize data produced by the Java VM options -Xloggc:<file> or jstat > <file>.
 * 
 * Code license:	Apache License 2.0
 * 
 * http://code.google.com/p/gclogviewer
 */
package code.google.gclogviewer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class GCLogData{
	
	// key: time  value: consumetime
	private Map<String, String> ygcPauseTimes=new HashMap<String, String>();
	
	// key: time value: String[0]: memoryBefore String[1]: memoryAfter
	private Map<String, String[]> ygcMemoryChanges=new HashMap<String, String[]>();
	
	// key: time  value: consumetime
	private Map<String, String> fgcPauseTimes=new HashMap<String, String>();
	
	// key: time value: String[0]: memoryBefore String[1]: memoryAfter
	private Map<String, String[]> fgcMemoryChanges=new HashMap<String, String[]>();
	
	// key: time  value: pausetime
	private Map<String, String> cmsGCPauseTimes=new HashMap<String, String>();
	
	// key: time value: String[0]: memoryBefore String[1]: memoryAfter
	private Map<String, String[]> cmsGCMemoryChanges=new HashMap<String, String[]>();
	
	// YGC Live data size
	private Map<String, String> ygcLDS=new HashMap<String, String>();
	
	// YGC Promote to old size
	private Map<String,String> ygcPTOS=new HashMap<String, String>();
	
	private String gcType;
	
	private DecimalFormat doubleformat=new DecimalFormat("#0.000");
	
	private String runtime;

	public Map<String, String[]> getCMSGCMemoryChanges() {
		return cmsGCMemoryChanges;
	}

	public void setCMSGCMemoryChanges(Map<String, String[]> cmsGCMemoryChanges) {
		this.cmsGCMemoryChanges = cmsGCMemoryChanges;
	}
	
	public String getRuntime() {
		return runtime + " secs";
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public Map<String, String> getYGCPauseTimes() {
		return ygcPauseTimes;
	}

	public Map<String, String[]> getYGCMemoryChanges() {
		return ygcMemoryChanges;
	}

	public Map<String, String> getFGCPauseTimes() {
		return fgcPauseTimes;
	}

	public Map<String, String[]> getFGCMemoryChanges() {
		return fgcMemoryChanges;
	}

	public Map<String, String> getCMSGCPauseTimes() {
		return cmsGCPauseTimes;
	}

	public void setYGCPauseTimes(Map<String, String> ygcPauseTimes) {
		this.ygcPauseTimes = ygcPauseTimes;
	}

	public void setYGCMemoryChanges(Map<String, String[]> ygcMemoryChanges) {
		this.ygcMemoryChanges = ygcMemoryChanges;
	}

	public void setFGCPauseTimes(Map<String, String> fgcPauseTimes) {
		this.fgcPauseTimes = fgcPauseTimes;
	}

	public void setFGCMemoryChanges(Map<String, String[]> fgcMemoryChanges) {
		this.fgcMemoryChanges = fgcMemoryChanges;
	}

	public void setCMSGCPauseTimes(Map<String, String> cmsGCPauseTimes) {
		this.cmsGCPauseTimes = cmsGCPauseTimes;
	}

	public String getThroughput() {
		// (1-(YGCT+FGCT+CMSGCT)/RUNTIME)*100%
		return doubleformat.format((1-((getGCTDouble(ygcPauseTimes)+getGCTDouble(fgcPauseTimes)+getGCTDouble(cmsGCPauseTimes))/Double.parseDouble(runtime)))*100)+"%";
	}
	
	public int getFGC(){
		return fgcPauseTimes.size();
	}
	
	public String getFGCT(){
		double pauseTimes=getGCTDouble(fgcPauseTimes);
		return doubleformat.format(pauseTimes)+" secs";
	}
	
	public String getAvgFGCRate(){
		if(getFGC()==0)
			return "0 secs";
		// last FGC time / FGC
		double maxFGCHappenTime=0;
		for (String happenTime : fgcPauseTimes.keySet()) {
			if(maxFGCHappenTime < Double.parseDouble(happenTime)){
				maxFGCHappenTime = Double.parseDouble(happenTime); 
			}
		}
		return doubleformat.format(maxFGCHappenTime/getFGC())+" secs";
	}
	
	public String getAvgFGCT(){
		if(getFGC() == 0){
			return "0 secs";
		}
		double pauseTimes=getGCTDouble(fgcPauseTimes);
		return doubleformat.format(pauseTimes/getFGC())+" secs";
	}
	
	public int getCMSGC(){
		return cmsGCPauseTimes.size();
	}
	
	public String getCMSGCT(){
		double pauseTimes=getGCTDouble(cmsGCPauseTimes);
		return doubleformat.format(pauseTimes)+" secs";
	}
	
	public String getAvgCMSGCRate(){
		if(getCMSGC()==0)
			return "0 secs";
		// last CMSGC time / CMSGC
		double maxCMSGCHappenTime=0;
		for (String happenTime : cmsGCPauseTimes.keySet()) {
			if(maxCMSGCHappenTime < Double.parseDouble(happenTime)){
				maxCMSGCHappenTime = Double.parseDouble(happenTime); 
			}
		}
		return doubleformat.format(maxCMSGCHappenTime/getCMSGC())+" secs";
	}
	
	public String getAvgCMSGCT(){
		double pauseTimes=getGCTDouble(cmsGCPauseTimes);
		return doubleformat.format(pauseTimes/getCMSGC())+" secs";
	}
	
	public int getYGC(){
		return ygcPauseTimes.size();
	}
	
	public String getYGCT(){
		double pauseTimes = getGCTDouble(ygcPauseTimes);
		return doubleformat.format(pauseTimes)+" secs";
	}
	
	public String getAvgYGCRate(){
		if(getYGC()==0)
			return "0 secs";
		// last YGC time / YGC
		double maxYGCHappenTime=0;
		for (String happenTime : ygcPauseTimes.keySet()) {
			if(maxYGCHappenTime < Double.parseDouble(happenTime)){
				maxYGCHappenTime = Double.parseDouble(happenTime); 
			}
		}
		return doubleformat.format(maxYGCHappenTime/getYGC())+" secs";
	}

	public String getAvgYGCT(){
		double pauseTimes = getGCTDouble(ygcPauseTimes);
		return doubleformat.format(pauseTimes/getYGC())+" secs";
	}
	
	public String getGCType(){
		return gcType;
	}
	
	public void setGCType(String gcType){
		this.gcType=gcType;
	}
	
	public Map<String, String> getYGCLDS() {
		return ygcLDS;
	}
	
	public int getAVGYGCLDS(){
		if(ygcLDS.size()==0)
			return 0;
		int summaryLDS=0;
		for (String lds : ygcLDS.values()) {
			summaryLDS += Integer.parseInt(lds);
		}
		return summaryLDS/ygcLDS.size();	
	}
	
	public int getAVGFGCLDS(){
		if(getFGC()==0)
			return 0;
		int summaryLDS=0;
		for (String[] changes : fgcMemoryChanges.values()) {
			summaryLDS += Integer.parseInt(changes[1]);
		}
		return summaryLDS/fgcMemoryChanges.size();
	}
	
	public int getAVGPTOS(){
		if(ygcPTOS.size()==0)
			return 0;
		int summaryPTOS=0;
		for (String ptos : ygcPTOS.values()) {
			summaryPTOS += Integer.parseInt(ptos);
		}
		return summaryPTOS/ygcPTOS.size();
	}

	public Map<String, String> getYGCPTOS() {
		return ygcPTOS;
	}

	// caculate ygcLDS and ygcPTOS
	// YGC LDS: current (ygcHeapMemoryAfter) - previous(ygcHeapMemoryAfter - MemoryAfterGC)
	// YGC Promote to old size: current (ygcHeapMemoryAfter - MemoryAfterGC) - previous(ygcHeapMemoryAfter - MemoryAfterGC)
	public void setDataForLDSAndPTOS(String currentYGCHeapMemoryAfter,String currentMemoryAfterGC,
								     String previousYGCHeapMemoryAfter,String previousMemoryAfterGC,
								     String happenTime) {
		String ygcLDSInfo = String.valueOf(Integer.parseInt(currentYGCHeapMemoryAfter) - (Integer.parseInt(previousYGCHeapMemoryAfter)-Integer.parseInt(previousMemoryAfterGC)));
		String ygcPTOSInfo = String.valueOf((Integer.parseInt(currentYGCHeapMemoryAfter)-Integer.parseInt(currentMemoryAfterGC)) - (Integer.parseInt(previousYGCHeapMemoryAfter)-Integer.parseInt(previousMemoryAfterGC)));
		ygcLDS.put(happenTime, ygcLDSInfo);
		ygcPTOS.put(happenTime, ygcPTOSInfo);
	}
	
	private double getGCTDouble(Map<String,String> pauseTimesMap) {
		double pauseTimes=0;
		for (String pauseTime : pauseTimesMap.values()) {
			pauseTimes+=Double.parseDouble(pauseTime);
		}
		return pauseTimes;
	}

}
