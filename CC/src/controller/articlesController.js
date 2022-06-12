const db = require("../config/database");
const { nanoid } = require("nanoid");
const express = require("express");
const moment = require("moment");
const uploadImage = require("../helpers/helpers");
const multer = require("multer");
const app = express();

const multerMid = multer({
  storage: multer.memoryStorage(),
  limits: {
    // no larger than 5mb.
    fileSize: 5 * 1024 * 1024,
  },
});
app.use(multerMid.single("file"));

//Get All Article
const getAllArticles = (req, res) => {
  const querySql = "SELECT * FROM `articles` order by 'createdAt' Desc";
  db.query(querySql, (err, rows, field) => {
    // error handling
    if (err) {
      return res.status(500).json({
        message: "Ada kesalahan",
        error: err,
      });
    }
    res.json({
      status: "OK",
      data: rows,
      methode: req.method,
      url: req.url,
    });
  });
};

//get Article By Id
const getArticleById = (req, res) => {
  const id = req.params.id;
  const querySql =
    "SELECT * FROM `articles` WHERE `articleId` LIKE '" + id + "'";

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
        id: id,
        message: "Id tidak ditemukan",
        methode: req.method,
        url: req.url,
      });
    }
    res.json({
      id: id,
      status: "OK",
      data: rows,
      methode: req.method,
      url: req.url,
    });
  });
};

//get Article By Tags
const getArticleByTags = (req, res) => {
  const tags = req.params.tags;
  const querySql = "SELECT * FROM `articles` WHERE `tags` LIKE '" + tags + "'";

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
        tags: tags,
        message: "tags tidak ditemukan",
        methode: req.method,
        url: req.url,
      });
    }
    res.json({
      tags: tags,
      status: "OK",
      data: rows,
      methode: req.method,
      url: req.url,
    });
  });
};
``;

//post article
const postArticle = async (req, res, next) => {
  try {
    const { title, tags, body } = req.body;
    const id = nanoid(16);
    const createdAt = moment().format("YYYY-MM-DD HH:mm:ss");
    const updatedAt = createdAt;
    const myFile = req.file;
    console.log(myFile);
    
    const imageUrl = await uploadImage(myFile);

    const querySql =
      "INSERT INTO `articles` (`articleId`, `title`, `tags`, `image`, `body`, `createdAt`, `updatedAt`) VALUES ('" +
      id +
      "', '" +
      title +
      "', '" +
      tags +
      "', '" +
      imageUrl +
      "', '" +
      body +
      "','" +
      createdAt +
      "', '" +
      updatedAt +
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
      res.json({
        status: "created",
        message: "Data berhasil disimpan",
        data: req.body,
        methode: req.method,
        url: req.url,
      });
    });
  } catch (error) {
    next(error);
  }
};

//edit
const editArticle = (req, res) => {
  const id = req.params.id;
  const { title, image, tags, body } = req.body;
  const updateAt = moment().format("YYYY-MM-DD HH:mm:ss");

  const querySql =
    "UPDATE `articles` SET `title` = '" +
    title +
    "', `tags` = '" +
    tags +
    "', `image` = '" +
    image +
    "', `body` = '" +
    body +
    "', `updatedAt` = '" +
    updateAt +
    "' WHERE `articleId` LIKE '" +
    id +
    "';";
  const querySearch =
    "SELECT * FROM `articles` WHERE `articleId` LIKE '" + id + "'";

  db.query(querySearch, (err, rows, field) => {
    // error handling
    if (err) {
      return res.status(500).json({ message: "Ada kesalahan", error: err });
    }

    // jika id yang dimasukkan sesuai dengan data yang ada di db
    if (rows.length) {
      // jalankan query update
      db.query(querySql, (err, rows, field) => {
        // error handling
        if (err) {
          return res.status(500).json({ message: "Ada kesalahan", error: err });
        }

        // jika update berhasil
        res.status(200).json({
          id: id,
          status: "Edited",
          message: "Data dengan berhasil diedit",
          methode: req.method,
          url: req.url,
        });
      });
    } else {
      return res.status(404).json({
        id: id,
        message: "Data tidak ditemukan!",
        methode: req.method,
        url: req.url,
      });
    }
  });
};

const deleteArticle = (req, res) => {
  const id = req.params.id;
  const querySearch =
    "SELECT * FROM `articles` WHERE `articleId` LIKE '" + id + "'";
  const queryDelete =
    "DELETE FROM `articles` WHERE `articleId` LIKE '" + id + "'";

  // jalankan query untuk melakukan pencarian data
  db.query(querySearch, (err, rows, field) => {
    // error handling
    if (err) {
      return res.status(500).json({ message: "Ada kesalahan", error: err });
    }

    // jika id yang dimasukkan sesuai dengan data yang ada di db
    if (rows.length > 0) {
      console.log(rows.length);
      // jalankan query delete
      db.query(queryDelete, (err, rows, field) => {
        // error handling
        if (err) {
          return res.status(500).json({ message: "Ada kesalahan", error: err });
        }

        // jika delete berhasil
        res.status(200).json({
          id: id,
          status: "Deleted",
          message: "Berhasi Hapus data",
          methode: req.method,
          url: req.url,
        });
      });
    } else {
      return res.status(404).json({
        message: "Data tidak ditemukan!",
        id: id,
        methode: req.method,
        url: req.url,
      });
    }
  });
};

module.exports = {
  getAllArticles,
  getArticleById,
  getArticleByTags,
  postArticle,
  editArticle,
  deleteArticle,
};
