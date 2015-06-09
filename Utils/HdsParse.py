# --------------------------------------------------
#
#	execfile("C:/playground/live/HdsParse.py")
#	parseAbst("C:/temp/chunks/bytes/asdasd")
#
# ---------------------------------------------------

from construct import *

ServerEntry = Struct("ServerEntry",
	CString("ServerBaseUrl", terminators = b"\0"),
)
QualityEntry = Struct("QualityEntry",
	CString("QualitySegmentUrlModifier", terminators = b"\0"),
)

SegmentRunEntry = Struct("SegmentRunEntry",
	UBInt32("FirstSegment"),
	UBInt32("FragmentsPerSegment")
)

SegmentRunTable = Struct("SegmentRunTable",
	Bytes("Header",4),
	BitStruct("bit",
		BitField("Version", 8),
		BitField("Flags", 24)
	),
	UBInt8("QualityEntryCount"),
	Array(lambda ctx: ctx.QualityEntryCount, CString("QualitySegmentUrlModifiers", terminators = b"\0")),
	UBInt32("SegmentRunEntryCount"),
	Array(lambda ctx: ctx.SegmentRunEntryCount, SegmentRunEntry),
)

FragmentRunEntry = Struct("FragmentRunEntry",
	UBInt32("FirstFragment"),
	UBInt64("FirstFragmentTimeStamp"),
	UBInt32("FragmentDuration")
)

FragmentRunTable = Struct("FragmentRunTable",
	Bytes("Header",4),
	BitStruct("bit",
		BitField("Version", 8),
		BitField("Flags", 24)
	),
	UBInt32("TimeScale"),
	UBInt8("QualityEntryCount"),
	Array(lambda ctx: ctx.QualityEntryCount, CString("QualitySegmentUrlModifiers", terminators = b"\0")),
	UBInt32("FragmentRunEntryCount"),
	Array(lambda ctx: ctx.FragmentRunEntryCount, FragmentRunEntry),
)

abstStruct = Struct("abst",
	UBInt32("WTF"),
	Bytes("Header",4),
	BitStruct("bit",
		BitField("Version", 8),
		BitField("Flags", 24),
		BitField("BootstrapinfoVersion",32),
		BitField("Profile",2),
		BitField("Live",1),
		BitField("Update",1),
		BitField("Reserved",4)
	),
	UBInt32("TimeScale"),
	UBInt64("CurrentMediaTime"),
	UBInt64("SmpteTimeCodeOffset"),
	CString("MovieIdentifier", terminators = b"\0"),
	UBInt8("ServerEntryCount"),
	Array(lambda ctx: ctx.ServerEntryCount, ServerEntry),
	UBInt8("QualityEntryCount"),
	Array(lambda ctx: ctx.QualityEntryCount, QualityEntry),
	CString("DrmData", terminators = b"\0"),
	CString("Metadata", terminators = b"\0"),
	UBInt8("SegmentRunTableCount"),
	Padding(4),
	Array(lambda ctx: ctx.SegmentRunTableCount, SegmentRunTable),
	UBInt8("FragmentRunTableCount"),
	Padding(4),
	Array(lambda ctx: ctx.FragmentRunTableCount, FragmentRunTable),
)

def pretty(d, indent=0):
	res = ""
	for key, value in d.iteritems():
		if isinstance(value, ListContainer):
			res = res + '\t' * indent + str(key) + ":"
			i = 0
			for val in value:
				res = res + '\t' * (indent + 1) + "[" + str(i) + "]\n" 
				if(type(val) in (int, float, bool, str)):
					res = res + '\t' * (indent+2) + str(val) + "\n"
				else:
					res = res + pretty(val, indent+2) + "\n"
				i = i + 1
		else:
			if key != '__recursion_lock__':
				if isinstance(value, dict):
					res = res + pretty(value, indent) + "\n"
				else :	
					res = res + '\t' * indent + str(key) + " = " + str(value) + "\n"
	return res

def parseAbst(file):
	f = open(file, "rb")
	f.seek(0)
	res = abstStruct.parse(f.read())
	resStr = pretty(res)
	f.close()
	return resStr

def writeToFile(file, content):
	f = open(file,'w')
	f.write(content)
	f.close()
