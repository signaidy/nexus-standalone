import { PUBLIC_BACKEND_URL } from '$env/static/public';


 export async function load({ fetch }) {
     const response = await fetch(`${PUBLIC_BACKEND_URL}/aboutus/1`);
     const aboutus = await response.json();
     return {
        aboutus,
     };
   }
  

export const actions = {
    default: async({request, cookies}) => {
        const formData = await request.formData(); 
        const token = cookies.get('token');
        const response = await fetch(`${PUBLIC_BACKEND_URL}/aboutus/1`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
          slogan: formData.get("slogan"),
          gif: formData.get("gif"),
          yt: formData.get("yt"),
          cardsAmount: formData.get("cardsamount"),
          titleOne: formData.get("t1"), 
          textOne: formData.get("tx1"), 
          imgOne: formData.get("img1"), 
          titleTwo: formData.get("t2"), 
          textTwo: formData.get("tx2"), 
          imgTwo: formData.get("img2"), 
          titleThree: formData.get("t3"), 
          textThree: formData.get("tx3"), 
          imgThree: formData.get("img3"), 
          titleFour: formData.get("t4"), 
          textFour: formData.get("tx4"), 
          imgFour: formData.get("img4"), 

        }),
      });
      const result = await response.json();
      if(!result.ok){
        return {
            message: "About us information updated"
        }
      } else {
        return{ 
            message: "Error while updating the information"
        }
      }


    }
}