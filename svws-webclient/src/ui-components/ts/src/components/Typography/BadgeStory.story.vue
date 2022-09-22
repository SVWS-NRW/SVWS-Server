<script setup lang="ts">
import SvwsUiBadge from "./Badge.vue";
</script>

<template>
  <Story title="SVWS UI/Typography/Badge" :layout="{type: 'grid', width: '40%'}">
    <Variant title="Primary">
      <svws-ui-badge variant="primary" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Success">
      <svws-ui-badge variant="success" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Error">
      <svws-ui-badge variant="error" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Highlight">
      <svws-ui-badge variant="highlight" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Light">
      <svws-ui-badge variant="light" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Small">
      <svws-ui-badge variant="primary" size="small">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Normal">
      <svws-ui-badge variant="primary" size="normal">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Medium">
      <svws-ui-badge variant="primary" size="medium">Badge</svws-ui-badge>
    </Variant>

    <Variant title="Big">
      <svws-ui-badge variant="primary" size="big">Badge</svws-ui-badge>
    </Variant>
  </Story>
</template>