<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Listado de clientes</title>
                <style>
                    body {
                    font-family: Arial, sans-serif;
                    margin: 30px;
                    background-color: #f4f6f8;
                    }
                    h1 {
                    color: #243b53;
                    }
                    table {
                    width: 100%;
                    border-collapse: collapse;
                    background-color: white;
                    }
                    th, td {
                    border: 1px solid #ddd;
                    padding: 12px;
                    text-align: left;
                    }
                    th {
                    background-color: #2563eb;
                    color: white;
                    }
                    tr:nth-child(even) {
                    background-color: #f2f2f2;
                    }
                </style>
            </head>
            <body>
                <h1>Listado de clientes</h1>

                <table>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Email</th>
                    </tr>

                    <xsl:for-each select="clientes/cliente">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><xsl:value-of select="nombre"/></td>
                            <td><xsl:value-of select="email"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>