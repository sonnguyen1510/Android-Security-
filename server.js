/**
 * var http = require('http');
const PORT = 8080;
const server = http.createServer(function (request, response) {
  response.writeHead(200, {'Content-Type': 'text/plain'});
  response.end('Hello World');
}).listen(PORT,()=> {console.log('Server running at ${PORT}')}); 


const User =[
  { name: 'John', address: 'Highway 71'},
  { name: 'Peter', address: 'Lowstreet 4'},
  { name: 'Amy', address: 'Apple st 652'},
  { name: 'Hannah', address: 'Mountain 21'},
  { name: 'Michael', address: 'Valley 345'},
  { name: 'Sandy', address: 'Ocean blvd 2'},
  { name: 'Betty', address: 'Green Grass 1'},
  { name: 'Richard', address: 'Sky st 331'},
  { name: 'Susan', address: 'One way 98'},
  { name: 'Vicky', address: 'Yellow Garden 2'},
  { name: 'Ben', address: 'Park Lane 38'},
  { name: 'William', address: 'Central st 954'},
  { name: 'Chuck', address: 'Main Road 989'},
  { name: 'Viola', address: 'Sideway 1633'}
]
//{User:{name:"Son Nguyen",Age:21,Phone:"083623237623",password:"mypass",Favorite:[{Sport:"Swimming",Food:"Potato chip"},{Sport:"Swimming",Food:"Potato chip"}]}};

localhost:3000/User
function CheckUser(sport){
   return User.Favorite.Sport === sport
}
app.get('/User', (req, res) => {
  res.send(User)
})

app.get('/User/:Name', (req, res) => {
  const user = User.find(User => User.name === req.params.Name.toString())
  if(!user){
    res.status(404).send('Not Found')
  }
  res.send(user)
})

//post

app.post('/User/add', function (req, res) {
  const addUser = {
    name : req.body.name,
    address : req.body.address
  }
  User.push(addUser)
  res.send(JSON.stringify({
    Success : true,
    Notes : "Add successful",
    Data : User
  }))
})

//put

app.put('/User/edit/:name/:address',function (req,res) {
  const findUser = User.find(User => User.name === req.params.name.toString());
  findUser.address = req.params.address;
  res.send(JSON.stringify({
    Success : true,
    Notes : "Edit successful",
    Data : User
  }))
})

//delete


app.delete('/User/delete/:name/:num_of_element', function(req, res) {
  const deleteUser = User.find(User => User.name === req.params.name)

  const location = User.indexOf(deleteUser)
  User.splice(location,parseInt(req.params.num_of_element))
  res.send(JSON.stringify({
    Success : true,
    Notes : "Delete successful",
    DeleteData : deleteUser,
    Data : User

  }))
});
*/
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


//Setup router
app.use('/api/v1/User',UserRouter)
app.use('/api/v1/Admin',AdminRouter)
app.use('/api/v1/upload',)


app.listen(3000, () => console.log(`Example app listening on port 3000!`))

module.exports = {app};
