// src/hooks.server.ts
import { redirect, type Handle } from "@sveltejs/kit";
import { httpRequests, httpDuration } from "$lib/server/metrics";

export const handle: Handle = async ({ event, resolve }) => {
  // --- Your existing Stage 1 (auth) logic ---
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
      role: (user.role.replace("ROLE_", "") as "USER" | "ADMIN" | "EMPLOYEE"),
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

  // --- Metrics timing ---
  const route = event.route?.id ?? 'unmatched';
  const method = event.request.method;
  const endTimer = httpDuration.startTimer({ route, method });

  const response = await resolve(event);

  const status = response.status.toString();
  httpRequests.inc({ route, method, status }, 1);
  endTimer({ route, method, status });

  return response;
};