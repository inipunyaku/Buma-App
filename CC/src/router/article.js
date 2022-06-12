const { request } = require("express");
const express = require("express");
const router = express.Router();
const articleController = require("../controller/articlesController");

router
  .route("/articles")
  .get(articleController.getAllArticles)
  .post(articleController.postArticle);

router.get("/articles/id/:id", articleController.getArticleById);
router.get("/articles/tags/:tags", articleController.getArticleByTags);
router.put("/articles/:id", articleController.editArticle);

router.delete("/articles/:id", articleController.deleteArticle);

module.exports = router;
