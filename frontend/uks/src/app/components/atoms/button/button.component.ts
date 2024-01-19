import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {

  @Input() text!: string;
  @Input() type: string = 'submit';
  @Input() defaultClass: string = '';
  @Input() extenseClass: string = '';
  @Input() disabled: boolean = false;
  @Output() buttonClick: EventEmitter<void> = new EventEmitter<void>();

  constructor() { }

  ngOnInit(): void {
  }

  onClick(): void {
    this.buttonClick.emit();
  }

}
