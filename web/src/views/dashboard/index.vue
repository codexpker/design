<template>
  <div id="building">
    <div class="dashboard-container">
      <div class="dashboard-text">欢迎 {{ name }} 用户</div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Dashboard",
  computed: {
    ...mapGetters(["name"]),
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 35px;
    text-decoration-style: solid;
    line-height: 46px;
    color: #fefac7;
  }
}

#building {
  background-image: url("../../assets/k-on02.jpg");
  background-size: auto;
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;
  position: fixed;
}
</style>
