import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss'],
})
export class InputComponent implements OnInit {
  @Input() placeholder: string = '';
  @Input() label: string = '';
  @Input() required: boolean = false;
  @Input() id: string = '';
  @Input() invalid: boolean = false;
  @Input() errorMessage: string = '';
  @Input() control!: FormControl;
  @Input() defaultClass: string = '';
  @Input() extenseClass: string = '';
  @Input() type: string = 'text';

  constructor() {}

  ngOnInit(): void {}
}
