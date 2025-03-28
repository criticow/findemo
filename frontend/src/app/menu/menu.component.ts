import { Component, ElementRef, HostListener, inject } from "@angular/core";
import { ListIconComponent } from "../common/icons/list.icon";
import { CommonModule } from "@angular/common";
import { Router, RouterLink, RouterLinkActive } from "@angular/router";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  standalone: true,
  imports: [ListIconComponent, CommonModule, RouterLink, RouterLinkActive]
})
export class MenuComponent {
  public isOpen: boolean;
  private el: ElementRef;
  private router: Router;

  constructor() {
    this.el = inject(ElementRef);
    this.router = inject(Router);
    this.isOpen = false;
  }

  toggleMenu() {
    this.isOpen = !this.isOpen;
  }

  public logout() {
    localStorage.removeItem("token");
    this.isOpen = false;
    this.router.navigate(['/signin']);
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (this.isOpen && !this.el.nativeElement.contains(event.target)) {
      this.isOpen = false;
      console.log("hello")
    }
  }
}