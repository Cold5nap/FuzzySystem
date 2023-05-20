<template>
  <div>
    <Header></Header>
    <router-view />
    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";


export default {
  name: "AppComponent",
  components: {
    Header,
    Footer
  },
}
</script>
