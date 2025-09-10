import { redirect } from "@sveltejs/kit";

export function load({ locals }) {
  return {
    user: locals.user,
  };
}

export const actions = {
  logOut: async ({ cookies }) => {
    cookies.delete("token", { path: "/" });
    cookies.delete("User", { path: "/" });
    redirect(303, "/");
  },
};
