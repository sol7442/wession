<!-- METADATA TYPE="typelib" FILE="C:\Program files\common Files\system\ado\msado15.dll" -->
<%@language="VBScript" CodePage="65001"%>
<%
Option Explicit

const JSON_OBJECT = 0
const JSON_ARRAY = 1
const modULUS_BITS = 512
const CONGRUENT_BITS = 448
const BITS_to_A_BYTE = 8
const BYTES_to_A_WorD = 4
const BITS_to_A_WorD = 32
const GC_KEY = "c4CdB8D38Ba692031093d0AD36fD9249/+"
const sBASE_64_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

'
class DBHelper
	private defaultconnstring, defaultconnection, rs, cmd
	Private source
	private sub class_initialize
		defaultconnstring="provider=sqloledb;data source=bicsb2b.co.kr;initial catalog=hahobj7;user id=hahobj7 ;password=haho1092"
		set defaultconnection=nothing
	end sub

	public function ExecSPReturnRS(spname,params,connectionstring)
		if isobject(connectionstring) then
			if connectionstring is nothing then
				if defaultconnection is nothing then
					set defaultconnection=createobject("adodb.connection")
					defaultconnection.open defaultconnstring
				end if
				set connectionstring=defaultconnection
			end if
		end if
		set rs=server.createobject("adodb.recordset")
		set cmd=createobject("adodb.command")
		cmd.activeconnection = connectionstring
		cmd.commandtext=spname
		cmd.commandtype=adcmdstoredproc
		set cmd=collectparams(cmd,params)
		rs.cursorlocation=aduseclient
		rs.open cmd,,adopenstatic,adlockreadonly
		dim i
		for i=0 to cmd.parameters.count-1
			if cmd.parameters(i).direction=adparamoutput or cmd.parameters(i).direction=adparaminputoutput or cmd.parameters(i).direction=adparamreturnvalue then
				if isobject(params) then
					if params is nothing then
						exit for
					end if
				else
					params(i)(4)=cmd.parameters(i).value
				end if
			end if
		next
		set cmd.activeconnection=nothing
		set cmd=nothing
		if rs.state=adstateclosed then
			set rs.source=nothing
		end if
		set rs.activeconnection=nothing
		set ExecSPReturnRs = rs
	end function

	public sub ExecSP(spname,params,connectionstring)
		if isobject(connectionstring) then
			if connectionstring is nothing then
				if defaultconnection is nothing then
					set defaultconnection = createobject("adodb.connection")
					defaultconnection.open defaultconnstring
				end if
				set connectionstring=defaultconnection
			end if
		end if
		set cmd=createobject("adodb.command")
			cmd.activeconnection=connectionstring
			cmd.commandtext=spname
			cmd.commandtype=adcmdstoredproc
		set cmd=collectparams(cmd,params)
			cmd.execute ,,adexecutenorecords
		dim i
		for i=0 to cmd.parameters.count-1
			if cmd.parameters(i).direction=adparamoutput or cmd.parameters(i).direction=adparaminput or cmd.parameters(i).direction=adparamreturnvalue then
				if IsObject(params) then
					if params is nothing then
						exit for
					end if
				else
					params(i)(4)=cmd.parameters(i).value
				end if
			end if
		next
		set cmd.activeconnection=nothing
		set cmd=nothing
	end sub

	public function makeparam(pname,ptype,pdirection,psize,pvalue)
		makeparam=array(pname,ptype,pdirection,psize,pvalue)
	end function

	public function getvalue(params,paramname)
		dim param
		for each param in params
			if param(0)=paramname then
				getvalue=param(4)
				exit function
			end if
		next
	end function

	private function collectParams(cmd,argparams)
		dim i,l,u,v
		If VarType(argparams) = 8192 or VarType(argparams) = 8204 or VarType(argparams) = 8209 then
			dim params:params = argparams
			For i = LBound(params) To UBound(params)
				l = LBound(params(i))
				u = UBound(params(i))
				' Check for nulls.
				If u - l = 4 Then
					If VarType(params(i)(4)) = vbString Then
						If params(i)(4) = "" Then
							v = Null
						Else
							v = params(i)(4)
						end If
					Else
						v = params(i)(4)
					end If
					cmd.Parameters.Append cmd.CreateParameter(params(i)(0), params(i)(1), params(i)(2), params(i)(3), v)
				end If
			next

			set collectParams = cmd
			Exit function
		Else
			set collectParams = cmd
		end If
	end function

	public sub dispose()
		if (not defaultconnection is nothing) then
			if (defaultconnection.state=adstateopen) then defaultconnection.close
			set defaultconnection=nothing
		end if
	end sub

	public function connectionState
		dim conn:conn=createobject("adodb.connection")
		conn.open defaultconnstring
		connectionState=conn.state
		conn.close
		set conn=nothing
	end function
end Class

'
class HashHelper
	private m_lOnBits(30),m_l2Power(30),K(63)

	private sub class_initialize()
		m_lOnBits(0) = clng(1)
		m_lOnBits(1) = clng(3)
		m_lOnBits(2) = clng(7)
		m_lOnBits(3) = clng(15)
		m_lOnBits(4) = clng(31)
		m_lOnBits(5) = clng(63)
		m_lOnBits(6) = clng(127)
		m_lOnBits(7) = clng(255)
		m_lOnBits(8) = clng(511)
		m_lOnBits(9) = clng(1023)
		m_lOnBits(10) = clng(2047)
		m_lOnBits(11) = clng(4095)
		m_lOnBits(12) = clng(8191)
		m_lOnBits(13) = clng(16383)
		m_lOnBits(14) = clng(32767)
		m_lOnBits(15) = clng(65535)
		m_lOnBits(16) = clng(131071)
		m_lOnBits(17) = clng(262143)
		m_lOnBits(18) = clng(524287)
		m_lOnBits(19) = clng(1048575)
		m_lOnBits(20) = clng(2097151)
		m_lOnBits(21) = clng(4194303)
		m_lOnBits(22) = clng(8388607)
		m_lOnBits(23) = clng(16777215)
		m_lOnBits(24) = clng(33554431)
		m_lOnBits(25) = clng(67108863)
		m_lOnBits(26) = clng(134217727)
		m_lOnBits(27) = clng(268435455)
		m_lOnBits(28) = clng(536870911)
		m_lOnBits(29) = clng(1073741823)
		m_lOnBits(30) = clng(2147483647)
		m_l2Power(0) = clng(1)
		m_l2Power(1) = clng(2)
		m_l2Power(2) = clng(4)
		m_l2Power(3) = clng(8)
		m_l2Power(4) = clng(16)
		m_l2Power(5) = clng(32)
		m_l2Power(6) = clng(64)
		m_l2Power(7) = clng(128)
		m_l2Power(8) = clng(256)
		m_l2Power(9) = clng(512)
		m_l2Power(10) = clng(1024)
		m_l2Power(11) = clng(2048)
		m_l2Power(12) = clng(4096)
		m_l2Power(13) = clng(8192)
		m_l2Power(14) = clng(16384)
		m_l2Power(15) = clng(32768)
		m_l2Power(16) = clng(65536)
		m_l2Power(17) = clng(131072)
		m_l2Power(18) = clng(262144)
		m_l2Power(19) = clng(524288)
		m_l2Power(20) = clng(1048576)
		m_l2Power(21) = clng(2097152)
		m_l2Power(22) = clng(4194304)
		m_l2Power(23) = clng(8388608)
		m_l2Power(24) = clng(16777216)
		m_l2Power(25) = clng(33554432)
		m_l2Power(26) = clng(67108864)
		m_l2Power(27) = clng(134217728)
		m_l2Power(28) = clng(268435456)
		m_l2Power(29) = clng(536870912)
		m_l2Power(30) = clng(1073741824)
		K(0) = &H428A2F98
		K(1) = &H71374491
		K(2) = &HB5C0FBCF
		K(3) = &HE9B5DBA5
		K(4) = &H3956C25B
		K(5) = &H59F111F1
		K(6) = &H923F82A4
		K(7) = &HAB1C5ED5
		K(8) = &HD807AA98
		K(9) = &H12835B01
		K(10) = &H243185BE
		K(11) = &H550C7DC3
		K(12) = &H72BE5D74
		K(13) = &H80DEB1FE
		K(14) = &H9BDC06A7
		K(15) = &HC19BF174
		K(16) = &HE49B69C1
		K(17) = &HEFBE4786
		K(18) = &HFC19DC6
		K(19) = &H240CA1CC
		K(20) = &H2DE92C6F
		K(21) = &H4A7484AA
		K(22) = &H5CB0A9DC
		K(23) = &H76F988DA
		K(24) = &H983E5152
		K(25) = &HA831C66D
		K(26) = &HB00327C8
		K(27) = &HBF597FC7
		K(28) = &HC6E00BF3
		K(29) = &HD5A79147
		K(30) = &H6CA6351
		K(31) = &H14292967
		K(32) = &H27B70A85
		K(33) = &H2E1B2138
		K(34) = &H4D2C6DFC
		K(35) = &H53380D13
		K(36) = &H650A7354
		K(37) = &H766A0ABB
		K(38) = &H81C2C92E
		K(39) = &H92722C85
		K(40) = &HA2BFE8A1
		K(41) = &HA81A664B
		K(42) = &HC24B8B70
		K(43) = &HC76C51A3
		K(44) = &HD192E819
		K(45) = &HD6990624
		K(46) = &HF40E3585
		K(47) = &H106AA070
		K(48) = &H19A4C116
		K(49) = &H1E376C08
		K(50) = &H2748774C
		K(51) = &H34B0BCB5
		K(52) = &H391C0CB3
		K(53) = &H4ED8AA4A
		K(54) = &H5B9CCA4F
		K(55) = &H682E6FF3
		K(56) = &H748F82EE
		K(57) = &H78A5636F
		K(58) = &H84C87814
		K(59) = &H8CC70208
		K(60) = &H90BEFFFA
		K(61) = &HA4506CEB
		K(62) = &HBEF9A3F7
		K(63) = &HC67178F2
	end sub

	private function LShift(lValue, iShiftBits)
		if iShiftBits = 0 then
			LShift = lValue
			exit function
		elseif iShiftBits = 31 then
			if lValue and 1 then
				LShift = &H80000000
			else
				LShift = 0
			end if
			exit function
		elseif iShiftBits < 0 or iShiftBits > 31 then
			Err.Raise 6
		end if
		if (lValue and m_l2Power(31 - iShiftBits)) then
			LShift = ((lValue and m_lOnBits(31 - (iShiftBits + 1))) * m_l2Power(iShiftBits)) or &H80000000
		else
			LShift = ((lValue and m_lOnBits(31 - iShiftBits)) * m_l2Power(iShiftBits))
		end if
	end function

	private function RShift(lValue, iShiftBits)
		if iShiftBits = 0 then
			RShift = lValue
			exit function
		elseif iShiftBits = 31 then
			if lValue and &H80000000 then
				RShift = 1
			else
				RShift = 0
			end if
			exit function
		elseif iShiftBits < 0 or iShiftBits > 31 then
			Err.Raise 6
		end if

		RShift = (lValue and &H7FFFFFFE) \ m_l2Power(iShiftBits)
		if (lValue and &H80000000) then
			RShift = (RShift or (&H40000000 \ m_l2Power(iShiftBits - 1)))
		end if
	end function

	private function RotateLeft(lValue, iShiftBits)
		RotateLeft = LShift(lValue, iShiftBits) or RShift(lValue, (32 - iShiftBits))
	end function

	private function AddUnsigned(lX, lY)
		dim lX4
		dim lY4
		dim lX8
		dim lY8
		dim lResult
		lX8 = lX and &H80000000
		lY8 = lY and &H80000000
		lX4 = lX and &H40000000
		lY4 = lY and &H40000000
		lResult = (lX and &H3FFFFFFF) + (lY and &H3FFFFFFF)
		if lX4 and lY4 then
			lResult = lResult xor &H80000000 xor lX8 xor lY8
		elseif lX4 or lY4 then
			if lResult and &H40000000 then
				lResult = lResult xor &HC0000000 xor lX8 xor lY8
			else
				lResult = lResult xor &H40000000 xor lX8 xor lY8
			end if
		else
			lResult = lResult xor lX8 xor lY8
		end if

		AddUnsigned = lResult
	end function

	private function m_F(x, y, z)
		m_F = (x and y) or ((not x) and z)
	end function

	private function m_G(x, y, z)
		m_G = (x and z) or (y and (not z))
	end function

	private function m_H(x, y, z)
		m_H = (x xor y xor z)
	end function

	private function m_I(x, y, z)
		m_I = (y xor (x or (not z)))
	end function

	private function Ch(x, y, z)
		Ch = ((x and y) xor ((not x) and z))
	end function

	private function Maj(x, y, z)
		Maj = ((x and y) xor (x and z) xor (y and z))
	end function

	private function S(x, n)
		S = (RShift(x, (n and m_lOnBits(4))) or LShift(x, (32 - (n and m_lOnBits(4)))))
	end function

	private function R(x, n)
		R = RShift(x, clng(n and m_lOnBits(4)))
	end function

	private function Sigma0(x)
		Sigma0 = (S(x, 2) xor S(x, 13) xor S(x, 22))
	end function

	private function Sigma1(x)
		Sigma1 = (S(x, 6) xor S(x, 11) xor S(x, 25))
	end function

	private function Gamma0(x)
		Gamma0 = (S(x, 7) xor S(x, 18) xor R(x, 3))
	end function

	private function Gamma1(x)
		Gamma1 = (S(x, 17) xor S(x, 19) xor R(x, 10))
	end function

	private sub m_FF(a, b, c, d, x, s, ac)
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(m_F(b, c, d), x), ac))
		a = RotateLeft(a, s)
		a = AddUnsigned(a, b)
	end sub

	private sub m_GG(a, b, c, d, x, s, ac)
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(m_G(b, c, d), x), ac))
		a = RotateLeft(a, s)
		a = AddUnsigned(a, b)
	end sub

	private sub m_HH(a, b, c, d, x, s, ac)
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(m_H(b, c, d), x), ac))
		a = RotateLeft(a, s)
		a = AddUnsigned(a, b)
	end sub

	private sub m_II(a, b, c, d, x, s, ac)
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(m_I(b, c, d), x), ac))
		a = RotateLeft(a, s)
		a = AddUnsigned(a, b)
	end sub

	private function ConverttoWordArray(sMessage)
		dim lMessageLength
		dim lNumberOfWords
		dim lWordArray()
		dim lBytePosition
		dim lByteCount
		dim lWordCount
		lMessageLength = Len(sMessage)
		lNumberOfWords = (((lMessageLength + ((modULUS_BITS - CONGRUENT_BITS) \ BITS_to_A_BYTE)) \ (modULUS_BITS \ BITS_to_A_BYTE)) + 1) * (modULUS_BITS \ BITS_to_A_WorD)
		redim lWordArray(lNumberOfWords - 1)
		lBytePosition = 0
		lByteCount = 0
		do until lByteCount >= lMessageLength
			lWordCount = lByteCount \ BYTES_to_A_WorD
			lBytePosition = (lByteCount mod BYTES_to_A_WorD) * BITS_to_A_BYTE
			lWordArray(lWordCount) = lWordArray(lWordCount) or LShift(Asc(mid(sMessage, lByteCount + 1, 1)), lBytePosition)
			lByteCount = lByteCount + 1
		loop
		lWordCount = lByteCount \ BYTES_to_A_WorD
		lBytePosition = (lByteCount mod BYTES_to_A_WorD) * BITS_to_A_BYTE
		lWordArray(lWordCount) = lWordArray(lWordCount) or LShift(&H80, lBytePosition)
		lWordArray(lNumberOfWords - 2) = LShift(lMessageLength, 3)
		lWordArray(lNumberOfWords - 1) = RShift(lMessageLength, 29)
		ConverttoWordArray = lWordArray
	end function

	private function WordtoHex(lValue)
		dim lByte
		dim lCount
		for lCount = 0 to 3
			lByte = RShift(lValue, lCount * BITS_to_A_BYTE) and m_lOnBits(BITS_to_A_BYTE - 1)
			WordtoHex = WordtoHex & Right("0" & Hex(lByte), 2)
		next
	end function

	public function md5(sMessage)
		dim k,AA,BB,CC,DD
		dim x:x=ConverttoWordArray(sMessage)
		dim a:a=&H67452301
		dim b:b=&HEFCDAB89
		dim c:c=&H98BADCFE
		dim d:d=&H10325476
		dim S11:S11=7
		dim S12:S12=12
		dim S13:S13=17
		dim S14:S14=22
		dim S21:S21=5
		dim S22:S22=9
		dim S23:S23=14
		dim S24:S24=20
		dim S31:S31=4
		dim S32:S32=11
		dim S33:S33=16
		dim S34:S34=23
		dim S41:S41=6
		dim S42:S42=10
		dim S43:S43=15
		dim S44:S44=21
		dim ub:ub=ubound(x)
		for k=0 to ub step 16
			AA=a
			BB=b
			CC=c
			DD=d
			m_FF a, b, c, d, x(k + 0), S11, &HD76AA478
			m_FF d, a, b, c, x(k + 1), S12, &HE8C7B756
			m_FF c, d, a, b, x(k + 2), S13, &H242070DB
			m_FF b, c, d, a, x(k + 3), S14, &HC1BDCEEE
			m_FF a, b, c, d, x(k + 4), S11, &HF57C0FAF
			m_FF d, a, b, c, x(k + 5), S12, &H4787C62A
			m_FF c, d, a, b, x(k + 6), S13, &HA8304613
			m_FF b, c, d, a, x(k + 7), S14, &HFD469501
			m_FF a, b, c, d, x(k + 8), S11, &H698098D8
			m_FF d, a, b, c, x(k + 9), S12, &H8B44F7AF
			m_FF c, d, a, b, x(k + 10), S13, &HFFFF5BB1
			m_FF b, c, d, a, x(k + 11), S14, &H895CD7BE
			m_FF a, b, c, d, x(k + 12), S11, &H6B901122
			m_FF d, a, b, c, x(k + 13), S12, &HFD987193
			m_FF c, d, a, b, x(k + 14), S13, &HA679438E
			m_FF b, c, d, a, x(k + 15), S14, &H49B40821
			m_GG a, b, c, d, x(k + 1), S21, &HF61E2562
			m_GG d, a, b, c, x(k + 6), S22, &HC040B340
			m_GG c, d, a, b, x(k + 11), S23, &H265E5A51
			m_GG b, c, d, a, x(k + 0), S24, &HE9B6C7AA
			m_GG a, b, c, d, x(k + 5), S21, &HD62F105D
			m_GG d, a, b, c, x(k + 10), S22, &H2441453
			m_GG c, d, a, b, x(k + 15), S23, &HD8A1E681
			m_GG b, c, d, a, x(k + 4), S24, &HE7D3FBC8
			m_GG a, b, c, d, x(k + 9), S21, &H21E1CDE6
			m_GG d, a, b, c, x(k + 14), S22, &HC33707D6
			m_GG c, d, a, b, x(k + 3), S23, &HF4D50D87
			m_GG b, c, d, a, x(k + 8), S24, &H455A14ED
			m_GG a, b, c, d, x(k + 13), S21, &HA9E3E905
			m_GG d, a, b, c, x(k + 2), S22, &HFCEFA3F8
			m_GG c, d, a, b, x(k + 7), S23, &H676F02D9
			m_GG b, c, d, a, x(k + 12), S24, &H8D2A4C8A
			m_HH a, b, c, d, x(k + 5), S31, &HFFFA3942
			m_HH d, a, b, c, x(k + 8), S32, &H8771F681
			m_HH c, d, a, b, x(k + 11), S33, &H6D9D6122
			m_HH b, c, d, a, x(k + 14), S34, &HFDE5380C
			m_HH a, b, c, d, x(k + 1), S31, &HA4BEEA44
			m_HH d, a, b, c, x(k + 4), S32, &H4BDECFA9
			m_HH c, d, a, b, x(k + 7), S33, &HF6BB4B60
			m_HH b, c, d, a, x(k + 10), S34, &HBEBFBC70
			m_HH a, b, c, d, x(k + 13), S31, &H289B7EC6
			m_HH d, a, b, c, x(k + 0), S32, &HEAA127FA
			m_HH c, d, a, b, x(k + 3), S33, &HD4EF3085
			m_HH b, c, d, a, x(k + 6), S34, &H4881D05
			m_HH a, b, c, d, x(k + 9), S31, &HD9D4D039
			m_HH d, a, b, c, x(k + 12), S32, &HE6DB99E5
			m_HH c, d, a, b, x(k + 15), S33, &H1FA27CF8
			m_HH b, c, d, a, x(k + 2), S34, &HC4AC5665
			m_II a, b, c, d, x(k + 0), S41, &HF4292244
			m_II d, a, b, c, x(k + 7), S42, &H432AFF97
			m_II c, d, a, b, x(k + 14), S43, &HAB9423A7
			m_II b, c, d, a, x(k + 5), S44, &HFC93A039
			m_II a, b, c, d, x(k + 12), S41, &H655B59C3
			m_II d, a, b, c, x(k + 3), S42, &H8F0CCC92
			m_II c, d, a, b, x(k + 10), S43, &HFFEFF47D
			m_II b, c, d, a, x(k + 1), S44, &H85845DD1
			m_II a, b, c, d, x(k + 8), S41, &H6FA87E4F
			m_II d, a, b, c, x(k + 15), S42, &HFE2CE6E0
			m_II c, d, a, b, x(k + 6), S43, &HA3014314
			m_II b, c, d, a, x(k + 13), S44, &H4E0811A1
			m_II a, b, c, d, x(k + 4), S41, &HF7537E82
			m_II d, a, b, c, x(k + 11), S42, &HBD3AF235
			m_II c, d, a, b, x(k + 2), S43, &H2AD7D2BB
			m_II b, c, d, a, x(k + 9), S44, &HEB86D391
			a = AddUnsigned(a, AA)
			b = AddUnsigned(b, BB)
			c = AddUnsigned(c, CC)
			d = AddUnsigned(d, DD)
		next
		md5 = LCase(WordtoHex(a) & WordtoHex(b) & WordtoHex(c) & WordtoHex(d))
	end function

	public function sha256(sMessage)
		dim HASH(7)
		dim M:M=ConverttoWordArray(sMessage)
		dim W(63)
		dim a,b,c,d,e,f,g,h,i,j,T1,T2
		dim ub:ub=ubound(M)
		HASH(0)=&H6A09E667
		HASH(1)=&HBB67AE85
		HASH(2)=&H3C6EF372
		HASH(3)=&HA54FF53A
		HASH(4)=&H510E527F
		HASH(5)=&H9B05688C
		HASH(6)=&H1F83D9AB
		HASH(7)=&H5BE0CD19

		for i=0 to ub step 16
			a=HASH(0)
			b=HASH(1)
			c=HASH(2)
			d=HASH(3)
			e=HASH(4)
			f=HASH(5)
			g=HASH(6)
			h=HASH(7)
			for j=0 to 63
				if j<16 then
					W(j) = M(j+i)
				else
					W(j)=AddUnsigned(AddUnsigned(AddUnsigned(Gamma1(W(j-2)),W(j-7)),Gamma0(W(j-15))), W(j-16))
				end if
				T1=AddUnsigned(AddUnsigned(AddUnsigned(AddUnsigned(h,Sigma1(e)),Ch(e,f,g)),K(j)),W(j))
				T2=AddUnsigned(Sigma0(a),Maj(a,b,c))
				h=g
				g=f
				f=e
				e=AddUnsigned(d, T1)
				d=c
				c=b
				b=a
				a=AddUnsigned(T1, T2)
			next
			HASH(0)=AddUnsigned(a, HASH(0))
			HASH(1)=AddUnsigned(b, HASH(1))
			HASH(2)=AddUnsigned(c, HASH(2))
			HASH(3)=AddUnsigned(d, HASH(3))
			HASH(4)=AddUnsigned(e, HASH(4))
			HASH(5)=AddUnsigned(f, HASH(5))
			HASH(6)=AddUnsigned(g, HASH(6))
			HASH(7)=AddUnsigned(h, HASH(7))
		next
		sha256=LCase(Right("00000000" & Hex(HASH(0)),8)&Right("00000000" & Hex(HASH(1)),8)&Right("00000000" & Hex(HASH(2)),8)&Right("00000000" & Hex(HASH(3)),8)&Right("00000000"  & Hex(HASH(4)),8)&Right("00000000" & Hex(HASH(5)),8)&Right("00000000" & Hex(HASH(6)),8)&Right("00000000" & Hex(HASH(7)),8))
	end Function

	function encode(sMessage)
		Dim lnPosition,lsResult,Char1,Char2,Char3,Char4,Byte1,Byte2,Byte3,SaveBits1,SaveBits2,lsGroupBinary,lsGroup64,lnMessage
		lnMessage=Len(sMessage)
		if (lnMessage Mod 3 > 0) Then	sMessage = sMessage & String(3 - (lnMessage Mod 3), " ")
			lsResult = ""
		For lnPosition = 1 To lnMessage Step 3
			lsGroup64 = ""
			lsGroupBinary = Mid(sMessage, lnPosition, 3)
			Byte1 = Asc(Mid(lsGroupBinary, 1, 1)): SaveBits1 = Byte1 And 3
			Byte2 = Asc(Mid(lsGroupBinary, 2, 1)): SaveBits2 = Byte2 And 15
			Byte3 = Asc(Mid(lsGroupBinary, 3, 1))
			Char1 = Mid(sBASE_64_CHARACTERS, ((Byte1 And 252) \ 4) + 1, 1)
			Char2 = Mid(sBASE_64_CHARACTERS, (((Byte2 And 240) \ 16) Or (SaveBits1 * 16) And &HFF) + 1, 1)
			Char3 = Mid(sBASE_64_CHARACTERS, (((Byte3 And 192) \ 64) Or (SaveBits2 * 4) And &HFF) + 1, 1)
			Char4 = Mid(sBASE_64_CHARACTERS, (Byte3 And 63) + 1, 1)
			lsGroup64 = Char1 & Char2 & Char3 & Char4
			lsResult = lsResult + lsGroup64
		Next
		encode = lsResult
	End Function

	function decode(sMessage)
		Dim lsResult,lnPosition,lsGroup64, lsGroupBinary,Char1, Char2, Char3, Char4,Byte1, Byte2, Byte3, lnMessage
		lnMessage=Len(sMessage)
		if (lnMessage Mod 4 > 0) Then sMessage = sMessage & String(4 - (lnMessage Mod 4), " ")
		lsResult = ""
		For lnPosition = 1 To lnMessage Step 4
			lsGroupBinary = ""
			lsGroup64 = Mid(sMessage, lnPosition, 4)
			Char1 = INSTR(sBASE_64_CHARACTERS, Mid(lsGroup64, 1, 1)) - 1
			Char2 = INSTR(sBASE_64_CHARACTERS, Mid(lsGroup64, 2, 1)) - 1
			Char3 = INSTR(sBASE_64_CHARACTERS, Mid(lsGroup64, 3, 1)) - 1
			Char4 = INSTR(sBASE_64_CHARACTERS, Mid(lsGroup64, 4, 1)) - 1
			Byte1 = Chr(((Char2 And 48) \ 16) Or (Char1 * 4) And &HFF)
			Byte2 = lsGroupBinary & Chr(((Char3 And 60) \ 4) Or (Char2 * 16) And &HFF)
			Byte3 = Chr((((Char3 And 3) * 64) And &HFF) Or (Char4 And 63))
			lsGroupBinary = Byte1 & Byte2 & Byte3
			lsResult = lsResult + lsGroupBinary
		Next
		decode = lsResult
	end function
end class

'
class JsonHelper
	public collection,count,quotedVars,kind

	private sub class_initialize()
		set collection=createobject("scripting.dictionary")
		quotedvars=true
		count=0
	end sub

	private sub class_terminate()
		set collection=nothing
	end sub

	private property get counter
		counter=count
		count=count+1
	end property

	public property let pair(p,v)
		if (isnull(p)) then p=counter
		collection(p)=v
	end property

	public property set pair(p,v)
		if (isnull(p)) then p=counter
			'if (typename(v)<>"JSONAPI") then "https://www.google.co.kr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=4&ved=0CEEQFjAD&url=http%3A%2F%2Fwww.shop-wiz.com%2Fboard%2Fmain%2Fview%2Froot%2Fasp2%2F74%2F0%2F1&ei=Hkb6Uf7jE4-KkgWK-YHYCA&usg=AFQjCNGOApLXRPf7JlMWQH0LkezPu8Xw-A&sig2=KIMIszxZWV8DyjAUe4OJnw&bvm=bv.50165853",d.dGI
			'	err.raise &hD,"class:class","Incompatible types:'"&typename(v)&"'"
			'end if
		set collection(p)=v
	end property

	public default property get pair(p)
		if (isnull(p)) then p=count-1
		if (isobject(collection(p))) then
			set pair=collection(p)
		else
			pair=collection(p)
		end if
	end property

	public sub clean
		collection.removeall
	end sub

	public sub remove(vprop)
		collection.remve vprop
	end sub

	private function jsencode(ustr)
		dim charmap(127),stack()
		charmap(8)="\b"
		charmap(9)="\t"
		charmap(10)="\n"
		charmap(12)="\f"
		charmap(13)="\r"
		charmap(34)="\"""
		charmap(47)="\/"
		charmap(92)="\\"
		dim i, charcode
		dim strlen:strlen=len(ustr)-1
		redim stack(strlen)
		for i=0 to strlen
			stack(i)=mid(ustr,i+1,1)
			charcode=ascw(stack(i)) and 65535
			if (charcode<127) then
				if (not isempty(charmap(charcode))) then
					stack(i)=charmap(charcode)
				elseif (charcode<32) then
					stack(i)="\u"&right("000" & hex(charcode),4)
				end if
			else
				stack(i)="\u"&right("000" & hex(charcode),4)
			end if
		next
		jsencode=join(stack,"")
	end function

	public function tojson(vpair)
		select case vartype(vpair)
			case 0
				tojson="null"
			case 1
				tojson="null"
			case 7
				tojson="""" & cstr(vpair) & """"
			case 8
				tojson="""" & jsencode(vpair) & """"
			case 9
				dim bFI:bFI=true
				dim i
				if (vpair.kind) then tojson=tojson & "[" else tojson=tojson & "{"
				for each i in vpair.collection
					if (bFI) then bFI=false else tojson=tojson&","
					if (vpair.kind) then
						tojson=tojson&tojson(vpair(i))
					else
						if (quotedvars) then
							tojson=tojson&""""&i&""":"&tojson(vpair(i))
						else
							tojson=tojson&i&":"&tojson(vpair(i))
						end if
					end if
				next
				if (vpair.kind) then tojson=tojson & "]" else tojson=tojson&"}"
			case 11
				if (vpair) then tojson="true" else tojson="false"
			case 12, 8192, 8204
				tojson=renderArray(vpair,1,"")
			case else
				tojson=replace(vpair,",",".")
		end select
	end function

	private function renderArray(arr,depth,parent)
		dim first:first=lbound(arr,depth)
		dim last:last=ubound(arr,depth)
		dim index,rendered
		dim limiter:limiter=","
		renderArray="["
		for index=first to last
			if (index=last) then
				limiter=""
			end if
			on error resume next
			rendered=renderArray(arr,depth+1,parent&index&",")
			if (err=9) then
				on error goto 0
				renderArray=renderArray&tojson(eval("arr("&parent&index&")"))&limiter
			else
				renderArray=renderArray&rendered&""&limiter
			end if
		next
		renderArray=renderArray&"]"
	end function

	public property get jsString
		jsString=tojson(me)
	end property

	public sub flush
		if (typename(response)<>"Empty") then
			response.write(jsString)
		elseif (WScript<>Empty) then
			WScript.Echo(jsString)
		end if
	end sub

	Public Function Clone
		Set Clone = ColClone(Me)
	End Function

	Private Function ColClone(v)
		Dim jsc, i
		Set jsc = new JSON
		jsc.Kind = v.Kind
		For Each i In v.Collection
			If IsObject(v(i)) Then
				Set jsc(i) = ColClone(v(i))
			Else
				jsc(i) = v(i)
			End If
		Next
		Set ColClone = jsc
	End function
end class

'
class UtilHelper

	private m_FilePath
	private m_Source
	private m_Target
	private m_PhysicalPath
	private m_FileName
	private m_Fso

	public property let filepath(path)
		m_FilePath = path
	end property

	public property let source(src)
		m_Source = src
	end property

	public property let target(trg)
		m_Target = trg
	end property

	public property let filename(fname)
		m_FileName = fname
	end property

	private sub class_initialize
	end sub

	public function identityNum()
		identityNum =  datediff("s", "1900-01-01", date) + fix(timer*100)
	end function

	public function uniqueCode()
		randomize
		dim i, c, r
		r = ""

		for i = 1 to 12
			if Int((2 * Rnd) + 1) = "1" then
				c = Int(((57 - 48 + 1) * Rnd) + 48)
			else
				c = Int(((90 - 65 + 1) * Rnd) + 65)
			end if
			r = r + chr(c)
		next
		uniqueCode = trim(left(r, 4) & "-" &  mid(r, 5, 4) & "-" & right(r, 4))
	end function

	public sub queryCollection(collect)
		dim item
		for each item in collect
			response.write item & " : " & request.querystring(item) & "<br>"
		next
	end sub

	public sub sqlQueryCollection(collect)
		dim item
		for each item in collect
			item = lcase(item)
			response.write "dim " & item & " : " & item & " = request.querystring(" & item & ") <br/>"
		next
	end sub

	public sub formCollection(collect)
		dim item
		for each item in collect
			response.write item & " : " & request.form(item) & "<br>"
		next
	end sub

	public sub sqlFormCollection(collect)
		dim item
		for each item in collect
			item = lcase(item)
			response.write "dim " & item & " : " & item & " = request.form(" & item & ") <br/>"
		next
	end sub

	public function aspToHtml

		'
		' create folder : convertHTML
		' permission readwrite
		'
		dim objWinHttp
		if m_FileName = "" Or IsNull(m_FileName) Or IsEmpty(m_FileName) then
			call debug("UtilHelper : filename is undefined, line: 920")
		end if
		if m_Source = "" Or IsNull(m_Source) Or IsEmpty(m_Source) then
			call debug("UtilHelper : source filename is undefined, line: 923")
		end if

		if m_Target = "" Or IsNull(m_Target) Or IsEmpty(m_Target) then
			m_Target = ""
		else
			m_Target = m_Target & "\"
		end if

		set objWinHttp = Server.CreateObject("WinHttp.WinHttpRequest.5.1")
		objWinHttp.Open "GET", m_Source, false
		objWinHttp.Send()
		call createFile(objWinHttp)
		set objWinHttp = nothing
	end function

	public function validHashCookies(cookie)
		dim hash : set hash = new HashHelper
		dim c : c = cookie & GC_KEY
		if request.Cookies("hash_code") <> hash.sha256(c) then
			validHashCookies = false
		else
			validHashCookies = true
		end if
		set hash = nothing
	end function

	public function monthLastDay(mon)
		' 호출시 기본으로 이전달 마지막을 리턴
		' -값일 경우 다음달 마지막 날짜를 리턴
		monthLastDay = DateSerial(Year(date), Month(date) - mon, 1-1)
	end function

	public function isMobile()
		dim browsers, i, user_agent
		browsers =  array("iPhone", "iPod", "IEMobile", "Mobile", "lgtelecom", "PPC", "BlackBerry", "SCH-", "SPH-", "LG-", "CANU", "IM-" ,"EV-","Nokia", "Mobi")

		For i = 0 To Ubound(browsers)
			user_agent = Lcase(browsers(i))
			If inStr(Request.ServerVariables("HTTP_USER_AGENT"), user_agent) > 0  then
				isMobile = True
				Exit Function
			End If
		Next
		isMobile = False
	end function

	public function stripHTML(h, t)
		t = replace(t, ",", "|")
		stripHTML = eregiReplace("<(/?)(?!/|" & t & ")([^<>]*)?>", "", h)
	end function

	private function eregiReplace(p, r, t)
		dim objReg : set objReg = New RegExp
		objReg.Pattern = p
		objReg.IgnoreCase = false
		objReg.Global = true
		eregiReplace = objReg.Replace(t, r)
		set objReg = Nothing
	end function

	private sub createFile(obj)

		dim fn : fn = Server.MapPath("/") & "\" &  m_Target & m_FileName
		response.write fn
		dim fso : set fso = server.CreateObject("Scripting.FileSystemObject")
		dim cfn : set cfn = fso.CreateTextFile(fn, true)
		set cfn = nothing

		dim ofn : set ofn = fso.openTextFile(fn, 8, true)
		ofn.writeLine mStreamBinaryToString(obj.responseBody)
		ofn.close
		set ofn = nothing
		set fso = nothing
	end sub

	private function mStreamBinaryToString(bin)
		' 미리 선언이 필요
		' const adtypetext = 2
		' const adtypebinary = 1

		dim BinaryStream
		Set BinaryStream = CreateObject("ADODB.Stream")
		BinaryStream.Type = adTYpeBinary
		BinaryStream.Open
		BinaryStream.Write bin
		BinaryStream.Position = 0
		BinaryStream.Type = adTypeText
		BinaryStream.Charset = "UTF-8"

		mStreamBinaryToString = BinaryStream.ReadText
		Set BinaryStream = Nothing
	end function

	private sub debug(msg)
		response.clear
		response.write msg
		response.end
	end sub
end class

Function jsObject
	Set jsObject = new JSONAPI
	jsObject.Kind = JSON_OBJECT
End Function

Function jsArray
	Set jsArray = new JSONAPI
	jsArray.Kind = JSON_ARRAY
End Function

Function toJSON(val)
	toJSON = (new JSONAPI).toJSON(val)
End Function

%>