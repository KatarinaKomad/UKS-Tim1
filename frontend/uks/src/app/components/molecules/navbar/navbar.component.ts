import { Component, Input, OnInit } from '@angular/core';
import { SelectionOptions, titleMapper } from 'src/models/navigation';
import { Router } from '@angular/router';



@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  @Input() drawer: any;

  selected!: SelectionOptions;
  SelectionOptions = SelectionOptions;
  title: string = '';

  constructor(private router: Router) { 
    this.getCurrentHref();
  }

  ngOnInit(): void {
  }

  navigate(option: SelectionOptions){
    this.selected = option; 
    this.router.navigate([option])
  }

  private getCurrentHref() : void{
    const href = this.router.url;
    this.selected = href as SelectionOptions;
    this.title = titleMapper(this.selected);
  }

  logout(): void {
    console.log('logout')
  }

}
