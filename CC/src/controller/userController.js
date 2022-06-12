const db = require("../config/database");
const { nanoid } = require("nanoid");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");

const signup = (req, res) => {
  const { name, email, password } = req.body;
  const password_hash = bcrypt.hashSync(password, 10);
  const id = nanoid(10);

  const querySql =
    "INSERT INTO `users` (`userId`, `name`, `email`, `password`) VALUES ('" +
    id +
    "', '" +
    name +
    "', '" +
    email +
    "', '" +
    password_hash +
    "');";
  db.query(querySql, (err, rows, field) => {
    // error handling
    if (err) {
      return res.status(500).json({
        message: "Ada kesalahan",
        error: err,
      });
    }
    // jika request berhasil
    res.status(201).json({
      status: "created",
      message: "Register Berhasi",
      data: req.body,
      methode: req.method,
      url: req.url,
    });
  });
};

const login = (req, res) => {
  const email = req.body.email;
  const password = req.body.password;
  const querySql = "SELECT * FROM `users` WHERE `email` LIKE '" + email + "'";

  db.query(querySql, (err, rows, field) => {
    // error handling
    if (err) {
      return res.status(500).json({
        message: "Ada kesalahan",
        error: err,
      });
    }
    if (rows.length < 1) {
      return res.status(404).json({
        message: "login gagal periksa kembali email dan password",
        methode: req.method,
        url: req.url,
      });
    }
    const pw_hash = rows[0]["password"];
    const verified = bcrypt.compareSync(password, pw_hash);

    if (verified) {
      jwt.sign(rows[0]["userId"], "secret", (err, token) => {
        expiresIn: 86400;
        if (err) {
          console.log(err);
          res.send(err);
          return;
        }
        const Token = token;
        console.log(Token);
        res.status(200).json({
          status: "OK",
          message: "login berhasil",
          token: Token,
          data: rows[0],
          methode: req.method,
          url: req.url,
        });
      });
    }
    if (!verified) {
      res.status(404).json({
        message: "login gagal periksa kembali email dan password",
        methode: req.method,
        url: req.url,
      });
      console.log(verified);
    }
  });
};

module.exports = { login, signup };
