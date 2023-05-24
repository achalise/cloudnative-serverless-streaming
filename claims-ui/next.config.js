/** @type {import('next').NextConfig} */
const rewrites = () => {
  return [
    {
      source: "/cats",
      destination: "https://meowfacts.herokuapp.com",
    },
    {
      source: "/retrieveClaimCount",
      destination: "http://localhost:8081/retrieveClaimCount"
    }
  ];
}
const nextConfig = {
  reactStrictMode: true,
  rewrites
}
module.exports = nextConfig
