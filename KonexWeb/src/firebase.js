import firebase from 'firebase';

const firebaseConfig = {
  apiKey: "AIzaSyCMreCBNLqgMgMSpGtS7656-vToWdJ-R90",
  authDomain: "konex-deba1.firebaseapp.com",
  databaseURL: "https://konex-deba1-default-rtdb.firebaseio.com",
  projectId: "konex-deba1",
  storageBucket: "konex-deba1.appspot.com",
  messagingSenderId: "12007182061",
  appId: "1:12007182061:web:3a903932fc3bd7368286e4",
  measurementId: "G-FVY6RXKEJ4"
};

  const firebaseApp = firebase.initializeApp(firebaseConfig);
  const db = firebaseApp.database()
  const auth = firebase.auth();

  export { db, auth };