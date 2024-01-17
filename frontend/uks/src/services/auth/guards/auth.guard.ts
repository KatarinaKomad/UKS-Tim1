import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';

import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../auth.service';

export const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  return router.parseUrl('/login');
};

export const repoGuard = async () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const repository: RepoBasicInfoDTO =
    router.getCurrentNavigation()?.extras?.state?.['repository'];
  authService.getLoggedUser().subscribe({
    next: (user: UserBasicInfo | undefined) => {
      if (!user || !repository) {
        router.navigate(['/not-found']);
        return;
      }
      if (repository.owner.id !== user.id && !repository.isPublic) {
        router.navigate(['/not-found']);
        return;
      }
    },
  });
};
