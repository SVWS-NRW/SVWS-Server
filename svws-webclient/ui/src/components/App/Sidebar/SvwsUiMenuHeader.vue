<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		collapsed?: boolean;
		user?: string;
		schule?: string;
		schema?: string;
	}>(), {
		collapsed: false,
		user: undefined,
		schule: undefined,
		schema: undefined,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		emit("click", event);
	}
</script>

<template>
	<a class="app--menu--initials"
		href="#" @click.prevent="onClick">
		<svws-ui-tooltip position="right" v-if="user" :indicator="false">
			<div class="app--menu--initials--icon">
				<template v-if="user.length > 5">{{ user.split(' ').map((username) => username[0]).join('').toUpperCase() }}</template>
				<template v-else>{{ user.slice(0, 2).toUpperCase() }}</template>
			</div>
			<template #content>
				<div class="app--menu--initials--label">
					Angemeldet als {{ user }}
					<template v-if="schule">
						<br>
						<span class="opacity-50">{{ schule }}</span>
					</template>
					<template v-if="schema">
						<br>
						<span class="opacity-50">DB: {{ schema }}</span>
					</template>
				</div>
			</template>
		</svws-ui-tooltip>
	</a>
</template>

<style lang="postcss">
.app--menu--initials {
	@apply flex flex-col items-center w-full justify-center mx-auto relative;

	.app--menu--initials--icon {
		@apply flex flex-col items-center w-full justify-center mx-auto relative;
		@apply rounded-lg overflow-hidden bg-white text-black dark:text-white dark:bg-white/5 border border-black/10 dark:border-white/10 font-bold;
		@apply w-12 h-12 xl:w-14 xl:h-14;
	}

	svg {
		@apply w-3/4 mx-auto hidden;
	}
}
</style>
