Vue.component("registration",{
  data: function(){
       return{
         userName: '',
         password:'',
         name: '',
         surname: '',
         date: '',
         confirmPassword:'',
         gender: '',
         role:'',
         loggedInUser: {},
         allFilled: "OK",
         userNameUnique: "OK",
         correctRepeatedPassword: "OK"
       }
  },
  template: ` <div class="registration_form clearfix">
            
                <div class="user-details">
                    <div class="input-box">
                        <label class="letters">First name*</label>
                        <input type="text" required placeholder="Enter your first name" v-model="name">
                    </div>
                    <div class="input-box">
                        <label class="letters">Last name*</label>
                        <input type="text" required placeholder="Enter your last name" v-model="surname">
                    </div>
                    <div class="input-box">
                        <label class="letters">Date of birth*</label>
                        <input type="date" value="1950-01-01" placeholder="dd-mm-yyyy" v-model="date" />
                    </div>
                    <div class="input-box">
                        <label class="letters">Username*</label>
                        <input type="text" required placeholder="Enter a unique username" v-model="userName" />
                    </div>
                    <div class="input-box">
                        <label class="letters">Password*</label>
                        <input type="text" required placeholder="Enter your password" required="" v-model="password"/>
                    </div>
                    <div class="input-box">
                        <label class="letters">Confirm password*</label>
                        <input type="text" required placeholder="Confirm your password" required="" v-model="confirmPassword"/>
                    </div>
                     <br>
             <div v-if="loggedInUser.role =='ADMINISTRATOR'">
               <select  v-model="role" >
                  <option value="" disabled selected hidden>Role</option>
                  <option value = "DELIVERER"> DELIVERER</option>
                  <option value = "MANAGER"> MANAGER</option>
               </select>
            </div>
                          <br>
            <select class="gender-selection" v-model="gender" >
               <option value="" disabled selected hidden>Gender</option>
               <option value = "MALE">Male</option>
               <option value = "FEMALE">Female</option>
            </select>
            <br>
                    <div class="button">
                        <input type="submit" value="Register" v-on:click="Validation">
                    </div>
                </div>
        </div>        
   `
 ,
 	mounted()
	{
	 axios 
	 .get('rest/testlogin')
	 .then(response =>
	 {
	 if(response.data !="Err:UserIsNotLoggedIn")
	     {
	       this.loggedInUser=response.data;
	     }
	    })
			.catch(function (error) {
				alert('Login for users is temporary unavailable');
		}
	);
},
 methods : {
     Validation: function(){

         if(this.name == ''){
            this.allFilled='You must enter your name!';
            alert(this.allFilled);
             return false;
         }
         if(this.surname == ''){
            this.allFilled='You must enter your surname!';
            alert(this.allFilled);
             return false;
         }
         if(this.date == ''){
            this.allFilled='Please select your birthday!';
            alert(this.allFilled);
             return false;
         }
         if(this.userName==''){
            this.allFilled='You must enter a username!';
            alert(this.allFilled);
            return false;
        }
         if(this.gender == ''){
            this.allFilled='Please select your gender!';
            alert(this.allFilled);
             return false;
         }
         if(this.password == ''){
            this.allFilled='Please enter a password!';
            alert(this.allFilled);

             return false;
         }
         if(this.password != this.confirmPassword){
            this.correctRepeatedPassword='Passwords should be same!';
            alert(this.correctRepeatedPassword);
             return false;
         }

         axios.post('rest/usernameExists', this.userName)
            .then(response=>{
                if(response.data===true){
                    this.userNameUnique = 'There is a user with the same username, please enter a unique username!';
                    alert(this.userNameUnique);
                    return false;
                }
                else{
                    this.RegisterCustomer();
                }

            }).catch()
         
     },
     RegisterCustomer: function(){
 	axios.post('rest/CustomerReg/', {"userName":this.userName, "name":this.name, "surname":this.surname, "password":this.password, "date":this.date, "gender":this.gender,"role":this.role })
 		.then(response => {
                alert('Successful customer registration!');

                axios.get('rest/login',{
                    params:
                    {
                    userName: this.userName,
                    password: this.password
                    }
                })
             .then(response => {
                    if (response.data == 'YOUR ACCOUNT DOES NOT EXIST IN THE SYSTEM, PLEASE REGISTER!') {
                    alert('Err: YOUR ACCOUNT DOES NOT EXIST IN THE SYSTEM, PLEASE REGISTER');
                    }
                    else{
                    alert('Successful user login!')
                    window.location.href = "/";
                    }
                    
                    })
               .catch(() => {
                    alert('Login for users is temporary unavailable')
                    window.location.href = "/"});
        })
        .catch(() => {
            alert('Registration for customers is temporary unavailable!')
            window.location.href = "/";
            });	
    }
}
});