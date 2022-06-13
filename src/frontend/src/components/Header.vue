<template>
  <div class="container">
    <header class="d-flex flex-wrap justify-content-center">
      <a href="/" class="d-flex align-items-center me-md-auto text-dark text-decoration-none">
        <img src="fav.png" alt="a" height="30" class="me-2">
        <span class="fs-4">Visual Fuzzy Control Language</span>
      </a>
      <ul class="nav nav-pills">
        <li class="nav-item"><router-link :to="{name:'about'}" class="nav-link">Описание</router-link></li>
      </ul>
    </header>
  </div>
  <hr>
</template>

<script>
export default {
  name: "HeaderComponent"
}
</script>