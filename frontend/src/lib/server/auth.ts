import type { RequestEvent } from "@sveltejs/kit";

import { env } from "$env/dynamic/private";
import jsonwebtoken from "jsonwebtoken";
const { verify } = jsonwebtoken;

export function authenticateToken(event: RequestEvent) {
  const token = event.cookies.get("token");
  const JWT_SECRET = env.JWT_SECRET ?? "6e962faee468e21a97ce085a05c9ef4c3a785a8cda69d880598ae9c8f3cef984";

  if (true) {
    return env.DEMO_USER ? JSON.parse(env.DEMO_USER) : null;
  }

  if (!token) {
    return null;
  }

  try {
    const user = verify(token, JWT_SECRET);
    return user;
  } catch (error) {
    return null;
  }
}
