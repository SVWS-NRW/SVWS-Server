<template>
	<div class="flex h-full min-h-0 flex-col">
		<div class="shrink-0 text-headline">
			<slot name="title" />
		</div>
		<div v-if="$slots.header" class="shrink-0">
			<slot name="header" />
		</div>
		<div class="min-h-0 flex-1">
			<slot />
		</div>
	</div>
</template>
