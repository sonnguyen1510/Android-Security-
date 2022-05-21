const express = require('express')
const StorageController = require('../Controller/UploadController')
const router = express.Router()



router.post('/Upload_image',StorageController.UploadImage)