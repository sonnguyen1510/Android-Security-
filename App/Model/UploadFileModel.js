const mongoose = require('mongoose')
const ImageFileSchema = new mongoose.Schema({
    
        name: String,
        img:
        {
            data: Buffer,
            contentType: String
        }
    
})
let UploadImage = mongoose.model("ImageModel",ImageFileSchema)
module.exports = {UploadImage}