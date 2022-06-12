const mysql = require("mysql");
const koneksi = mysql.createConnection({
  host: "127.0.0.1",
  user: "root",
  password: "",
  database: "bumaDb",
  multipleStatements: true,
});
// koneksi database
koneksi.connect((err) => {
  if (err) throw err;
  console.log("MySQL Connected...");
});
module.exports = koneksi;
