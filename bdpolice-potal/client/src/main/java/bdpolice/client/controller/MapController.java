package bdpolice.client.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapController {
  @FXML private WebView webView;
  private double zoom = 12;

  @FXML
  public void initialize() {
    WebEngine engine = webView.getEngine();
    engine.loadContent(HTML_TEMPLATE.replace("${ZOOM}", String.valueOf(zoom)));
  }

  public void zoomIn() { zoom+=1; reload(); }
  public void zoomOut() { zoom=Math.max(1, zoom-1); reload(); }
  public void street() { /* layer switch hook */ }
  public void sat() { /* layer switch hook */ }
  public void terrain() { /* layer switch hook */ }

  private void reload() {
    webView.getEngine().executeScript("map.setZoom("+zoom+")");
  }

  private static final String HTML_TEMPLATE = """
<!DOCTYPE html>
<html>
<head>
  <meta charset='utf-8'/>
  <meta name='viewport' content='width=device-width, initial-scale=1.0'>
  <link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>
  <style>html,body,#map{height:100%;margin:0} .avatar{border:2px solid #fff;border-radius:50%;box-shadow:0 0 6px rgba(0,0,0,.2)}</style>
</head>
<body>
<div id='map'></div>
<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>
<script>
  const map = L.map('map').setView([23.8103, 90.4125], ${ZOOM});
  window.map = map;
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {maxZoom: 19}).addTo(map);
  function marker(lat,lng,name){ return L.marker([lat,lng]).bindPopup(name).addTo(map); }
  marker(23.8103,90.4125,'Officer Rahman');
  marker(23.7808,90.4209,'Officer Khan');
  marker(23.7516,90.3936,'Officer Ali');
</script>
</body>
</html>
""";
}
