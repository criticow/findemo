import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";

export interface ToastMessage {
  id?: number;
  text: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  public $messages: Observable<ToastMessage[]>;
  private messagesSubject: BehaviorSubject<ToastMessage[]>;
  private duration: number;

  constructor(){
    this.messagesSubject = new BehaviorSubject<ToastMessage[]>([]);
    this.$messages = this.messagesSubject.asObservable();
    this.duration = 2000;
  }

  public getMessages(){
    return this.messagesSubject.getValue();
  }

  public showMessage(message: ToastMessage){
    const id = new Date().getTime();

    this.messagesSubject.next([...this.getMessages(), {...message, id: id}]);

    setTimeout(() => {
      const messages = this.getMessages();
      this.messagesSubject.next(messages.filter((message) => message.id !== id));
    }, this.duration);
  }
}