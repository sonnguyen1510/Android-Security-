
const multer = require('multer')
const {UploadImage} = require('../Model/UploadFileModel')

const Storage = multer.diskStorage({
    destination : 'uploads',
    filename :(req,file,cb)=>{
      cb(null,file.originalname)
  }
  })
  
const upload = multer({
    storage:Storage
  }).single('testImage')

const StorageController = {
    UploadImage : async(req,res)=>{
        upload(req,res,(err)=>{
            if(err){
                console.error
            }
            else{
                const newImage = new UploadImage({
                    name : req.body.name,
                    img :{
                        data : req.file.file,
                        contentType : 'image/png'
                    }
                })
                newImage.save()
                .then(()=>res.json({
                    success : true,
                    note: ' Upload Success'
                }))
                .catch((err)=>{
                    console.log('Upload image fail')
                })
            }
        })
    }
}

module.exports = StorageController