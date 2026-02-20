<!DOCTYPE html>
<html>
<head>
  <title>Canvas Disegnabile</title>
</head>
<body>
  <canvas id="drawingCanvas" width="500" height="400" style="border:1px solid #000;"></canvas>
  <br>
  <button onclick="saveCanvas()">Salva Canvas</button>

  <script>
    const canvas = document.getElementById("drawingCanvas");
    const ctx = canvas.getContext("2d");
    let isDrawing = false;

    // Disegno sulla canvas
    canvas.addEventListener("mousedown", (e) => {
      isDrawing = true;
      ctx.beginPath();
      ctx.moveTo(e.offsetX, e.offsetY);
    });

    canvas.addEventListener("mousemove", (e) => {
      if (isDrawing) {
        ctx.lineTo(e.offsetX, e.offsetY);
        ctx.stroke();
      }
    });

    canvas.addEventListener("mouseup", () => {
      isDrawing = false;
    });

    canvas.addEventListener("mouseout", () => {
      isDrawing = false;
    });

    // Salva la canvas
    function saveCanvas() {
      const image = canvas.toDataURL("image/png");
      console.log("Canvas salvata:", image);

      // Puoi inviare `image` al server
    }
  </script>
</body>
</html>