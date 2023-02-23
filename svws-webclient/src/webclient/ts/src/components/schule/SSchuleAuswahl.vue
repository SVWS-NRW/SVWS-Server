<template>
	<svws-ui-secondary-menu>
		<template #headline>Schule</template>
		<template #header />
		<template #content>
			<template v-for="item in routeSchule.menu" :key="item.name">
				<div class="secondary-menu--navigation container">
					<svws-ui-sidebar-menu-item v-if="item.hatSchulform() && item.hatEineKompetenz()" @click="router.push({ name: item.name })">
						<template #label> <span>{{ item.text }}</span> </template>
					</svws-ui-sidebar-menu-item>
				</div>
			</template>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { routeSchule } from "~/router/apps/RouteSchule";
	import { router } from "~/router/RouteManager";

</script>
