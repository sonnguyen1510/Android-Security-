const mongoose = require('mongoose')
const ImageFileSchema = new mongoose.Schema({
    
        name: String,
        desc: String,
        img:
        {
            data: Buffer,
            contentType: String
        }
    
})
let UploadImage = mongoose.model("Upload",ImageFileSchema)
module.exports = {UploadImage}