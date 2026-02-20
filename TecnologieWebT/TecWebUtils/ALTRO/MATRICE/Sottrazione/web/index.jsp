<%@ page session="true"%>
<html>
   <head>
      <title>Sottrazione di matrici 4x2</title>
		<link type="text/css" href="styles/default.css" rel="stylesheet"></link>
   		<script type="text/javascript" src="scripts/utils.js"></script>
		<script type="text/javascript" src="scripts/matrixCount.js"></script>
   </head>
<body>
	<div>
	Matrice A:<br>
		<input type="text" size="2" id="a0" onchange="checkMatrix()"> <input type="text" size="2" id="a1" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="a2" onchange="checkMatrix()"> <input type="text" size="2" id="a3" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="a4" onchange="checkMatrix()"> <input type="text" size="2" id="a5" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="a6" onchange="checkMatrix()"> <input type="text" size="2" id="a7" onchange="checkMatrix()"><br>
	</div>
		<div>
	Matrice B:<br>
		<input type="text" size="2" id="b0" onchange="checkMatrix()"> <input type="text" size="2" id="b1" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="b2" onchange="checkMatrix()"> <input type="text" size="2" id="b3" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="b4" onchange="checkMatrix()"> <input type="text" size="2" id="b5" onchange="checkMatrix()"><br>
		<input type="text" size="2" id="b6" onchange="checkMatrix()"> <input type="text" size="2" id="b7" onchange="checkMatrix()"><br>
	</div>
		<div>
	Matrice Risultato:<br>
		<input type="text" size="2" id="r0" readonly> <input type="text" size="2" id="r1" readonly><br>
		<input type="text" size="2" id="r2" readonly> <input type="text" size="2" id="r3" readonly><br>
		<input type="text" size="2" id="r4" readonly> <input type="text" size="2" id="r5" readonly><br>
		<input type="text" size="2" id="r6" readonly> <input type="text" size="2" id="r7" readonly><br>
	</div>
	<div id="secret" style=" display: none;">
		<input type="button" id="single" value="single" onclick="myFunction(this)"><input type="button" id="multi" value="multi" onclick="myFunction(this)">
	</div>
		<div>
	<br>
		<input type="hidden" size="2" id="h0"> <input type="hidden" size="2" id="h3"><br>
		<input type="hidden" size="2" id="h2"> <input type="hidden" size="2" id="h5"><br>
		<input type="hidden" size="2" id="h4"> <input type="hidden" size="2" id="h6"><br>
		<input type="hidden" size="2" id="h6"> <input type="hidden" size="2" id="h7"><br>
	</div>
</body>
</html>

