<template lang="pug">
.container
  header.d-flex.flex-wrap.justify-content-center
    a.d-flex.align-items-center.me-md-auto.text-dark.text-decoration-none(href="/")
      img.me-2(src="fav.png", alt="a", height="30")
      span.fs-4.h5 Visual Fuzzy Control Language
    ul.nav.nav-pills
      li.nav-item.h5
        router-link.nav-link(:to="{ name: 'about' }") Описание
hr.m-0
</template>

<script>
export default {
  name: 'HeaderComponent',
}
</script>
