<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Newsletter EventGURU</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            text-align: center;
            color: #005CB9;
        }
        h1 {
            color: #005CB9;
        }
        ul {
            list-style: none;
            padding: 0;
        }

        /* Per desktop */
        @media screen and (min-width: 768px) {
            body {
                font-size: 18px;
            }
            h1 {
                font-size: 36px;
            }
            ul {
                font-size: 18px;
            }
        }
    </style>
</head>
<body>
<div>
    <p>Gentile amministratore,</p>

    <p>
        L'infermiere <strong>${nomeInfermiere} ${cognomeInfermiere}</strong> è disponibile
        per essere assegnato ad una struttura.
    </p>

    <p>
        Buon lavoro,<br><i>Il team SwiftHealthPocket</i>
    </p>
</div>
</body>
</html>
