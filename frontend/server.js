const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;

// Serve os arquivos estáticos da pasta 'public' (ou onde estiver seu index.html)
app.use(express.static(__dirname));

// Redireciona todas as requisições para o 'index.html'
app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, 'index.html'));
});

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});
