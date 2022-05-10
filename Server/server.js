//connect to 
const express = require('express')
const mongoose = require('mongoose')
//const cors = require('cors')
const app = express()
 
app.use(express.json())
//fapp.use(cors())


const databaseConfig = require('./App/Config/DatabaseConfig')
const UserRouter = require('./App/Router/User')
const AdminRouter = require('./App/Router/Admin')
const multer = require('multer')

//const SystemConfig = require(__path_Config + 'DatabaseConfig')

//app.locals.SystemConfig = SystemConfig
//connect to MongoDB
mongoose.connect(`mongodb+srv://${databaseConfig.username}:${databaseConfig.password}@cluster0.3bhp2.mongodb.net/${databaseConfig.database}?retryWrites=true&w=majority`)
.then(()=> {
  console.log('database Connected')
})
.catch((error) =>{
  console.log('Error Connecting to database')
})

const Storga = multer.diskStorage({
  destination : 'uploads',
  filename :(req,file,cb)=>{
    cb(null,Date.now + file.originalname)
}
})

//Setup router
app.use('/api/v1/User',UserRouter)
app.use('/api/v1/Admin',AdminRouter)



app.listen(3000, () => console.log(`Example app listening on port 3000!`))

module.exports = {app};
