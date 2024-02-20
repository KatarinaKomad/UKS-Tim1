import { RepoBasicInfoDTO, RepoRequest } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';

import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';

import { AuthService } from '../auth.service';
import { RepoService } from 'src/services/repo/repo.service';

export const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  return router.parseUrl('/login');
};

export const repoGuard: CanActivateFn = async (route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const repoService = inject(RepoService);
  const router = inject(Router);


  authService.getLoggedUser().subscribe({
    next: (user: UserBasicInfo | undefined) => {
      const repoName = localStorage.getItem("repoName") as string;
      if (!repoName || !user?.id) return true;

      const repoRequest: RepoRequest = {
        name: repoName,
        ownerId: user?.id,
        description: '',
      }

      repoService.validateOverviewByRepoName(repoRequest).subscribe({
        next: (repository: RepoBasicInfoDTO | null) => {
          if (!repository) {
            return router.navigate(['/not-found']);
          }
          return true;
        }, error: () => {
          return false;
        }
      })
      return true;
    }, error: () => {
      return false;
    },
  });
  return true;
};


// function getRepoName(state: RouterStateSnapshot): string {
//   try {
//     return state.toString().replace('repository,', '').trim();
//   } catch (error) {
//     return '';
//   }
// }
