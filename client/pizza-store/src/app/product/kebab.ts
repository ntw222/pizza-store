export class Kebab {

  id:String;
  price:String;
  description: String;
  name: String;

  constructor(data:any) {
    this.id = data.id;
    this.price = data.price;
    this.description = data.description;
    this.name = data.name;
  }
}
