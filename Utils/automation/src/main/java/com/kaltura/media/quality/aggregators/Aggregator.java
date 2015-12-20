package com.kaltura.media.quality.aggregators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.tools.ant.DirectoryScanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import com.kaltura.media.quality.configurations.AggregatorConfig;
import com.kaltura.media.quality.configurations.TestConfig;
import com.kaltura.media.quality.utils.StringUtils;

abstract public class Aggregator {
	private static Map<String, AggregatedStream> aggregatedData = new HashMap<String, AggregatedStream>();
	private static Map<String, SegmentsFiles> segmentsFiles = new HashMap<String, SegmentsFiles>();

	private String path;
	private boolean skipHeaders;

	class AggregatedSegment extends HashMap<Long, Double> {
		private static final long serialVersionUID = 8571351989599227131L;

		public AggregatedSegment() {
		}
	}

	class AggregatedBitrate extends HashMap<String, AggregatedSegment> {
		private static final long serialVersionUID = -1988876126291416218L;
		private int bitrate;

		public AggregatedBitrate(int bitrate) {
			this.bitrate = bitrate;
		}

		public void add(long segmentNumber, String domain, double value) {
			AggregatedSegment aggregatedSegment;
			if (containsKey(domain)) {
				aggregatedSegment = get(domain);
			} else {
				aggregatedSegment = new AggregatedSegment();
				put(domain, aggregatedSegment);
			}

			aggregatedSegment.put(segmentNumber, value);
		}

		public void appendBitrate(List<String> record) {
			for (int i = 0; i < size(); i++) {
				record.add("" + bitrate);
			}
		}

		public void appendDomain(List<String> record) {
			for (String domain : keySet()) {
				record.add(domain);
			}
		}

		public String appendChart(String title, String destinationPath) throws IOException {
			
			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();

			AggregatedSegment aggregatedSegment;
			for (String domain : keySet()) {
				aggregatedSegment = get(domain);
				for (long segmentNumber : aggregatedSegment.keySet()) {
					categoryDataset.addValue(aggregatedSegment.get(segmentNumber), domain, "" + segmentNumber);
				}
			}
			
			JFreeChart lineChart = ChartFactory.createLineChart(title + " - bitrate [" + bitrate + "]", "Domains", "Diff", categoryDataset);

			int width = 1200;
			int height = 250;
			int quality = 1;
			
			String fileName = title + " " + bitrate;
			fileName = fileName.replaceAll("[\\s-]", "_");
			fileName += ".jpg";
			
			FileOutputStream outputStream = new FileOutputStream(destinationPath + "/" + fileName);
			ChartUtilities.writeChartAsJPEG(outputStream, quality, lineChart, width, height);
			outputStream.close();
			
			return fileName;
		}

		public void appendData(long segmentNumber, List<String> record) {
			for (AggregatedSegment aggregatedSegment : values()) {
				if (aggregatedSegment.containsKey(segmentNumber)) {
					record.add("" + aggregatedSegment.get(segmentNumber));
				} else {
					record.add("");
				}
			}
		}
	}

	class AggregatedValue extends HashMap<Integer, AggregatedBitrate> {
		private static final long serialVersionUID = -3511593060586917671L;

		private String title;

		public AggregatedValue(String title) {
			this.title = title;
		}

		public void add(long segmentNumber, int bitrate, String domain, double value) {
			AggregatedBitrate aggregatedBitrate;
			if (containsKey(bitrate)) {
				aggregatedBitrate = get(bitrate);
			} else {
				aggregatedBitrate = new AggregatedBitrate(bitrate);
				put(bitrate, aggregatedBitrate);
			}

			aggregatedBitrate.add(segmentNumber, domain, value);
		}

		public void appendTitle(List<String> record) {
			for (AggregatedBitrate aggregatedBitrate : values()) {
				for (int i = 0; i < aggregatedBitrate.size(); i++) {
					record.add(title);
				}
			}
		}

		public Map<String, String> appendCharts(String destinationPath) throws IOException {
			Map<String, String> charts = new HashMap<String, String>();
			for (AggregatedBitrate aggregatedBitrate : values()) {
				charts.put(aggregatedBitrate.appendChart(title, destinationPath), title);
			}
			return charts;
		}

		public void appendBitrates(List<String> record) {
			for (AggregatedBitrate aggregatedBitrate : values()) {
				aggregatedBitrate.appendBitrate(record);
			}
		}

		public void appendDomains(List<String> record) {
			for (AggregatedBitrate aggregatedBitrate : values()) {
				aggregatedBitrate.appendDomain(record);
			}
		}

		public void appendData(long segmentNumber, List<String> record) {
			for (AggregatedBitrate aggregatedBitrate : values()) {
				aggregatedBitrate.appendData(segmentNumber, record);
			}
		}
	}

	class AggregatedStream extends HashMap<String, AggregatedValue> {
		private static final long serialVersionUID = -3511593060586917671L;
		private Set<Long> segmentsNumbers = new TreeSet<Long>();
		private String streamName;

		public AggregatedStream(String streamName) {
			this.streamName = streamName;
		}

		public void add(long segmentNumber, String title, int bitrate, String domain, double value) {
			if (!segmentsNumbers.contains(segmentNumber)) {
				segmentsNumbers.add(segmentNumber);
			}

			AggregatedValue aggregatedValue;
			if (containsKey(title)) {
				aggregatedValue = get(title);
			} else {
				aggregatedValue = new AggregatedValue(title);
				put(title, aggregatedValue);
			}

			aggregatedValue.add(segmentNumber, bitrate, domain, value);
		}

		public void saveCSV() throws IOException {
			String destinationPath = TestConfig.get().getDestinationFolder();
			
			 FileWriter writer;
			 List<String> record;
			 String line;
			
			 writer = new FileWriter(destinationPath + "/" + streamName +
			 ".csv");
			
			
			 // write titles
			 record = new ArrayList<String>();
			 record.add("Segment Number");
			 for(AggregatedValue aggregatedValue : values()){
			 aggregatedValue.appendTitle(record);
			 }
			 line = "\"" + StringUtils.join(record, "\",\"") + "\"\n";
			 writer.write(line);
			
			 // write bitrates
			 record = new ArrayList<String>();
			 record.add("");
			 for(AggregatedValue aggregatedValue : values()){
			 aggregatedValue.appendBitrates(record);
			 }
			 line = StringUtils.join(record, ",") + "\n";
			 writer.write(line);
			
			 // write domains
			 record = new ArrayList<String>();
			 record.add("");
			 for(AggregatedValue aggregatedValue : values()){
			 aggregatedValue.appendDomains(record);
			 }
			 line = StringUtils.join(record, ",") + "\n";
			 writer.write(line);
			
			 // write data
			 for(long segmentNumber : segmentsNumbers){
				record = new ArrayList<String>();
				record.add("" + segmentNumber);
			 for(AggregatedValue aggregatedValue : values()){
			 aggregatedValue.appendData(segmentNumber, record);
			 }
			 line = StringUtils.join(record, ",") + "\n";
			 writer.write(line);
			 }
			
			 writer.close();
		}

		public void saveHTML() throws IOException {
			String destinationPath = TestConfig.get().getDestinationFolder();
			destinationPath += "/" + streamName;
			File dir = new File(destinationPath);
			if(!dir.exists()){
				dir.mkdirs();
			}

			Map<String, String> charts = new HashMap<String, String>();
			for (AggregatedValue aggregatedValue : values()) {
				charts.putAll(aggregatedValue.appendCharts(dir.getAbsolutePath()));
			}

			String html = "<html><body>";
			for(String chart : charts.keySet()){
				html += "<br/>";
				html += "<b>" + charts.get(chart) + "</b>";
				html += "<br/>";
				html += "<img src=\"" + streamName + "/" + chart + "\"/>";
				html += "<br/>";
			}
			html += "</body></html>";
			
			FileOutputStream outputStream = new FileOutputStream(destinationPath + ".html");
			outputStream.write(html.getBytes());
			outputStream.close();
		}

		public void save() throws IOException {
			saveCSV();
			saveHTML();
		}
	}

	class SegmentInfo{
		private long segmentNumber;
		private String domain;
		private int bitrate;

		public SegmentInfo(long segmentNumber, String domain, int bitrate) {
			this.segmentNumber = segmentNumber;
			this.domain = domain;
			this.bitrate = bitrate;
		}

		public long getSegmentNumber() {
			return segmentNumber;
		}

		public String getDomain() {
			return domain;
		}

		public int getBitrate() {
			return bitrate;
		}
	}

	class PendingData{

		private String title;
		private double value;

		public PendingData(String title, double value) {
			this.title = title;
			this.value = value;
		}

		public String getTitle() {
			return title;
		}

		public double getValue() {
			return value;
		}
	}

	class SegmentsFiles extends HashMap<String, SegmentInfo>{
		private static final long serialVersionUID = 2606832419294125863L;
		private String streamName;
		private Map<String, List<PendingData>>  pendingData = new HashMap<String, List<PendingData>>();

		public SegmentsFiles(String streamName){
			this.streamName = streamName;
		}
		
		public void handleData(String fileName, String title, double value) throws Exception {
			SegmentInfo segmentInfo;
			if(containsKey(fileName)){
				segmentInfo = get(fileName);
				addData(streamName, segmentInfo.getSegmentNumber(), segmentInfo.getDomain(), segmentInfo.getBitrate(), title, value);
			}
			else {
				List<PendingData> pendingDataList = new ArrayList<PendingData>();
				pendingDataList.add(new PendingData(title, value));
				pendingData.put(fileName, pendingDataList);
			}
		}

		public void add(String fileName, long segmentNumber, String domain, int bitrate) throws Exception {
			if(containsKey(fileName)){
				return;
			}
						
			put(fileName, new SegmentInfo(segmentNumber, domain, bitrate));
			if(pendingData.containsKey(fileName)){
				for(PendingData data : pendingData.remove(fileName)){
					addData(streamName, segmentNumber, domain, bitrate, data.getTitle(), data.getValue());
				}
			}
		}
	}
	
	class CSV extends ArrayList<String[]> {
		private static final long serialVersionUID = -3776477674988122754L;

		public CSV(String fileName) throws IOException {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line;
			String[] record;

			if (skipHeaders) {
				bufferedReader.readLine();
			}

			while ((line = bufferedReader.readLine()) != null) {
				record = line.split(",");
				add(record);
			}

			bufferedReader.close();
		}
	}

	public Aggregator(AggregatorConfig aggregatorConfig) {
		path = aggregatorConfig.getPath();
		skipHeaders = aggregatorConfig.getSkipHeaders();
	}

	abstract protected void aggregate(CSV csv, String streamName) throws Exception;

	protected void addData(String streamName, String fileName, String title, double value) throws Exception {
		SegmentsFiles streamSegmentsFiles;
		if (segmentsFiles.containsKey(streamName)) {
			streamSegmentsFiles = segmentsFiles.get(streamName);
		} else {
			streamSegmentsFiles = new SegmentsFiles(streamName);
			segmentsFiles.put(streamName, streamSegmentsFiles);
		}

		streamSegmentsFiles.handleData(fileName, title, value);
	}
	
	protected void addData(String streamName, String fileName, long segmentNumber, String domain, int bitrate, String title, double value) throws Exception {
		SegmentsFiles streamSegmentsFiles;
		if (segmentsFiles.containsKey(streamName)) {
			streamSegmentsFiles = segmentsFiles.get(streamName);
		} else {
			streamSegmentsFiles = new SegmentsFiles(streamName);
			segmentsFiles.put(streamName, streamSegmentsFiles);
		}
		streamSegmentsFiles.add(fileName, segmentNumber, domain, bitrate);
		
		addData(streamName, segmentNumber, domain, bitrate, title, value);
	}
	
	protected void addData(String streamName, long segmentNumber, String domain, int bitrate, String title, double value) {
		AggregatedStream aggregatedStream;
		if (aggregatedData.containsKey(streamName)) {
			aggregatedStream = aggregatedData.get(streamName);
		} else {
			aggregatedStream = new AggregatedStream(streamName);
			aggregatedData.put(streamName, aggregatedStream);
		}

		aggregatedStream.add(segmentNumber, title, bitrate, domain, value);
	}

	private void aggregate(File streamDir) throws Exception {
		String streamName = streamDir.getName();

		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { path });
		scanner.setBasedir(streamDir.getAbsolutePath() + "/logs/");
		scanner.setCaseSensitive(false);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		CSV csv;
		for (String filePath : files) {
			csv = new CSV(scanner.getBasedir() + "/" + filePath);
			aggregate(csv, streamName);
		}
	}

	public void aggregate() throws Exception {
		File dir = new File(TestConfig.get().getDestinationFolder() + "/content");
		File[] children = dir.listFiles();
		if (children != null) {
			for (File child : children) {
				if (!child.getName().equals(".")) {
					aggregate(child);
				}
			}
		}
	}

	public static void save() throws IOException {
		for (AggregatedStream aggregatedStream : aggregatedData.values()) {
			aggregatedStream.save();
		}
	}

	protected String trim(String string) {
		if (string.startsWith("\"")) {
			string = string.substring(1);
		}
		if (string.endsWith("\"")) {
			string = string.substring(0, string.length() - 1);
		}
		return string;
	}
}
