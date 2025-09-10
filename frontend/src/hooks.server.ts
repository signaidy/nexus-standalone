import { authenticateToken } from "$lib/server/auth";
import { redirect, type Handle } from "@sveltejs/kit";

export const handle: Handle = async ({ event, resolve }) => {
  console.log("handle");
  // Stage 1
  let userString  = event.cookies.get("User");
  const user = userString ? JSON.parse(userString) : null;
  if (user) {
    const mappedUser = {
      userId: user.id,
      email: user.email,
      firstName: user.firstName,
      iat: 0,
      lastName: user.lastName,
      originCountry: user.country,
      passportNumber: user.passport,
      role: user.role.replace("ROLE_", "") as "USER" | "ADMIN" | "EMPLOYEE",
      age: user.age.toString(),
      percentage: "100",
      entryDate: user.createdAt,
    };
    event.locals.user = mappedUser;
  }

  if (event.url.pathname.startsWith("/user")) {
    if (!event.locals.user) {
      throw redirect(303, "/login");
    }
    if (
      event.url.pathname.startsWith("/user/inventory") ||
      event.url.pathname.startsWith("/user/administration") ||
      event.url.pathname.startsWith("/user/purchase-logs") ||
      event.url.pathname.startsWith("/user/analytics")
    ) {
      if (event.locals.user.role !== "ADMIN") {
        throw redirect(303, "/login");
      }
    }
  }

  // Stage 2
  const response = await resolve(event);

  //Stage 3
  return response;
};
