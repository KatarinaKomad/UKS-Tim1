import { formFilter } from 'src/utils/custom-filters';

import { FormControl, Validators } from '@angular/forms';

import { LoginRequest, PasswordUpdateRequest } from '../authentication/login';
import { RegistrationRequest } from '../authentication/registration';
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

export const getLoginRequest = (loginForm: FormInput[]): LoginRequest => {
  return {
    email: formFilter(loginForm, 'email'),
    password: btoa(formFilter(loginForm, 'password')),
  };
};

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
    id: 'passwordConfirmation',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter confirm password',
    label: 'Confirm password',
    required: true,
    errorMessage: "'Invalid confirm password'",
    type: 'password',
  },
];

export const getRegistrationRequest = (
  signUpForm: FormInput[]
): RegistrationRequest => {
  return {
    email: formFilter(signUpForm, 'email'),
    password: btoa(formFilter(signUpForm, 'password')),
    passwordConfirmation: btoa(formFilter(signUpForm, 'passwordConfirmation')),
    firstName: formFilter(signUpForm, 'firstName'),
    lastName: formFilter(signUpForm, 'lastName'),
  };
};



export const updatePasswordFrom: FormInput[] = [
  {
    id: 'currentPassword',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter current password',
    label: 'Current password',
    required: true,
    errorMessage: "'Invalid password'",
    type: 'password',
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
    id: 'passwordConfirmation',
    control: new FormControl('', [Validators.required]),
    placeholder: 'Enter confirm password',
    label: 'Confirm password',
    required: true,
    errorMessage: "'Invalid confirm password'",
    type: 'password',
  },
];

export const getPasswordUpdateRequest = (updatePasswordFrom: FormInput[], email?: string): PasswordUpdateRequest => {
  return {
    email: email ? email : '',
    currentPassword: btoa(formFilter(updatePasswordFrom, 'currentPassword')),
    password: btoa(formFilter(updatePasswordFrom, 'password')),
    passwordConfirmation: btoa(formFilter(updatePasswordFrom, 'passwordConfirmation')),
  };
};
