import { FormControl, Validators } from '@angular/forms';
import { FormInput } from './input';

export const loginForm: FormInput[] = [
  {
    id: 'email',
    control: new FormControl('', [Validators.required, Validators.email]),
    placeholder: 'Enter email',
    label: 'Email',
    required: true,
    errorMessage: "'Invalid email'",
    type: 'text',
  },
  {
    id: 'password',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter password',
    label: 'Password',
    required: true,
    errorMessage: "'Invalid password'",
    type: 'password',
  },
];

export const signUpForm: FormInput[] = [
  {
    id: 'firstName',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter first name',
    label: 'First Name',
    required: true,
    errorMessage: "'First name required'",
    type: 'text',
  },
  {
    id: 'lastName',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Last name',
    label: 'Last Name',
    required: true,
    errorMessage: "'Last name required'",
    type: 'text',
  },
  {
    id: 'email',
    control: new FormControl('', [Validators.required, Validators.email]),
    placeholder: 'Enter email',
    label: 'Email',
    required: true,
    errorMessage: "'Invalid email'",
    type: 'text',
  },
  {
    id: 'password',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter password',
    label: 'Password',
    required: true,
    errorMessage: "'Invalid password'",
    type: 'password',
  },
  {
    id: 'confirmPassword',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter confirm password',
    label: 'Confirm password',
    required: true,
    errorMessage: "'Invalid confirm password'",
    type: 'password',
  },
];
