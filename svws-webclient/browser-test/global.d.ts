import type ErrorPage from "./src/PageError";

export {};

declare global {
 namespace PlaywrightTest {
    interface Matchers<R, T> {
        isErrorPageVisible(errorPage : ErrorPage): R;
    }
  }
}

export default global;