import type { RequestEvent } from "@sveltejs/kit";

import { JWT_SECRET } from "$env/static/private";
import jsonwebtoken from "jsonwebtoken";
const { verify } = jsonwebtoken;

export function authenticateToken(event: RequestEvent) {
  const token = event.cookies.get("token");

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
