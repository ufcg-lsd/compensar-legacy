var compression = require('compression');
var express = require('express');
var path = require('path');


var app = express();
app.use(compression());

app.use(express.static(__dirname + '/')); 
app.get('/*', function (req, res) { res.sendFile(path.join(__dirname,'/','index.html'));});

app.listen(process.env.PORT || 8000);