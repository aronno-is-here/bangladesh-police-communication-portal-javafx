package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapDialogController {
    @FXML private WebView webView;

    @FXML public void initialize() {
        WebEngine engine = webView.getEngine();
        String html = """
<!DOCTYPE html>
<html>
<head>
  <meta charset='utf-8'/>
  <meta name='viewport' content='width=device-width, initial-scale=1.0'>
  <link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>
  <style>html,body,#map{height:100%;margin:0} .badge{background:#1e40af;color:#fff;padding:4px 8px;border-radius:6px;font-family:Inter,Segoe UI,sans-serif}</style>
</head>
<body>
  <div id='map'></div>
  <script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>
  <script>
    const map = L.map('map').setView([23.8103, 90.4125], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {maxZoom: 19}).addTo(map);
    function addOfficer(lat,lng,name,status){
      const marker = L.marker([lat,lng]).addTo(map);
      marker.bindPopup(`<div class='badge'>${name} - ${status}</div>`);
      return marker;
    }
    addOfficer(23.8103,90.4125,'Officer Rahman','online');
    addOfficer(23.7808,90.4209,'Officer Khan','online');
    addOfficer(23.7516,90.3936,'Officer Ali','away');
  </script>
</body>
</html>
""";
        engine.loadContent(html);
    }

    @FXML public void lockMap() {
        // Could hide or prompt again
    }
}
